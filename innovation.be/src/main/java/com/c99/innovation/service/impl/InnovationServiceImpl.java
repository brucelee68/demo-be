package com.c99.innovation.service.impl;

import com.amazonaws.AmazonServiceException;
import com.c99.innovation.common.PageableBuilder;
import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.dto.AttachmentDTO;
import com.c99.innovation.dto.request.InnovationCreationRequest;
import com.c99.innovation.dto.request.InnovationUpdatingRequest;
import com.c99.innovation.dto.response.CommentResponse;
import com.c99.innovation.dto.response.InnovationDetailResponse;
import com.c99.innovation.dto.sortingfiltering.InnovationFilter;
import com.c99.innovation.dto.sortingfiltering.InnovationSpecification;
import com.c99.innovation.dto.sortingfiltering.PageDTO;
import com.c99.innovation.dto.sortingfiltering.QueryCriteria;
import com.c99.innovation.entity.*;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.mapper.InnovationMapper;
import com.c99.innovation.repository.*;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.AWSS3Service;
import com.c99.innovation.service.InnovationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InnovationServiceImpl implements InnovationService {

    private ModelMapper mapper;

    private InnovationMapper innovationMapper;

    private InnovationRepository innovationRepository;

    private AreaRepository areaRepository;

    private InnovationAreaRepository innovationAreaRepository;

    private AttachmentRepository attachmentRepository;

    private AWSS3Service awss3Service;

    private AccountRepository accountRepository;

    private ClapRepository clapRepository;

    private CommentRepository commentRepository;

    @Autowired
    public InnovationServiceImpl(
            ModelMapper modelMapper,
            InnovationMapper innovationMapper,
            InnovationRepository innovationRepository,
            AreaRepository areaRepository,
            InnovationAreaRepository innovationAreaRepository,
            AttachmentRepository attachmentRepository,
            AWSS3Service awss3Service,
            AccountRepository accountRepository,
            ClapRepository clapRepository,
            CommentRepository commentRepository) {
        this.mapper = modelMapper;
        this.innovationMapper = innovationMapper;
        this.innovationRepository = innovationRepository;
        this.areaRepository = areaRepository;
        this.innovationAreaRepository = innovationAreaRepository;
        this.attachmentRepository = attachmentRepository;
        this.awss3Service = awss3Service;
        this.accountRepository = accountRepository;
        this.clapRepository = clapRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public InnovationDetailResponse createInnovation(InnovationCreationRequest request) {

        List<Area> listArea = getListArea(request.getListAreaId());
        Innovation mappingInnovation = this.innovationMapper.convertToEntity(request);
        mappingInnovation.setStatus(Status.WAITING_FOR_APPROVE);
        Innovation savedInnovation = this.innovationRepository.save(mappingInnovation);

        List<InnovationArea> collects = listArea
                .stream().map(area -> new InnovationArea(savedInnovation, area))
                .collect(Collectors.toList());
        innovationAreaRepository.saveAll(collects);

        List<Attachment> attachments = request.getAttachmentList()
                .stream().map(attachment -> new Attachment(attachment.getActualName(), attachment.getUniqueName(), attachment.getUrl(), savedInnovation))
                .collect(Collectors.toList());
        attachmentRepository.saveAll(attachments);

        return this.innovationMapper.convertToDto(savedInnovation, listArea);
    }

    @Override
    public InnovationDetailResponse findById(Long id) {
        Innovation innovation = this.innovationRepository.findActiveInnovationById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
        List<Area> areaList = this.innovationAreaRepository.findAllAreaByInnovationId(innovation.getId());
        return this.innovationMapper.convertToDto(innovation, areaList);
    }

    @Transactional(rollbackFor = NonTransientDataAccessException.class)
    @Override
    public boolean deleteInnovation(long id) {
        Innovation i = innovationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if (i.getCreatedBy().getId() == principal.getAccountId() || a.getRole().getName().equals(RoleType.ADMIN.toString())){
            i.setStatus(Status.DELETED);
            innovationRepository.save(i);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = {RecordNotFoundException.class, DataIntegrityViolationException.class, NonTransientDataAccessException.class})
    @Override
    public InnovationDetailResponse updateInnovation(Long id, InnovationUpdatingRequest request) {
        List<Area> listArea = getListArea(request.getListAreaId());
        Innovation mappingInnovation = this.innovationMapper.convertToEntity(id, request);
        mappingInnovation.setStatus(Status.WAITING_FOR_APPROVE);
        Innovation savedInnovation = this.innovationRepository.save(mappingInnovation);

        this.innovationAreaRepository.removeAllByInnovationId(savedInnovation.getId());
        List<InnovationArea> collects = listArea
                .stream().map(area -> new InnovationArea(savedInnovation, area))
                .collect(Collectors.toList());
        innovationAreaRepository.saveAll(collects);
        this.deleteOldAttachmentsAndSaveNewAttachments(savedInnovation, request);
        return this.innovationMapper.convertToDto(savedInnovation, listArea);
    }

    @Override
    public PageDTO<InnovationDetailResponse> getAll(InnovationFilter innovationFilter, Optional<String> keyword, Optional<String> sortField, Optional<String> order, Optional<Integer> pageSize, Optional<Integer> pageIndex) {
        Pageable pageable = PageableBuilder.buildPage(order, sortField, pageSize, pageIndex);
        Specification<Innovation> combinedSpecification = this.createTheCombiningInnovationSpecification(innovationFilter, keyword);
        Page<Innovation> entityPage = innovationRepository.findAll(combinedSpecification, pageable);
        return this.innovationMapper.toPageDTO(entityPage);
    }

    @Transactional(rollbackFor = {NonTransientDataAccessException.class})
    @Override
    public boolean approveAnInnovation(Long id) {
        Innovation i = innovationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if (a.getRole().getName().equals(RoleType.ADMIN.toString()) ){
            i.setStatus(Status.APPROVED);
            innovationRepository.save(i);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = {NonTransientDataAccessException.class})
    @Override
    public boolean rejectAnInnovation(Long id) {
        Innovation i = innovationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if (a.getRole().getName().equals(RoleType.ADMIN.toString())){
            i.setStatus(Status.REJECTED);
            innovationRepository.save(i);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = {NonTransientDataAccessException.class})
    @Override
    public Long increaseClapInAnInnovation(Long id) {
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Clap clapByCurrentAccount = clapRepository.getClapByInnovationIdAndAccountId(id, principal.getAccountId());
        if(clapByCurrentAccount == null) {
            Innovation inno = this.innovationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
            Account account = this.accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
            clapRepository.save(new Clap(1L, StatusType.ACTIVE, inno, account));
            return 1L;
        } else if(clapByCurrentAccount.getClap() >= 50){
            return 50L;
        } else {
            this.clapRepository.increaseInnovationClapByAccount(clapByCurrentAccount.getId());
            return clapByCurrentAccount.getClap() + 1L;
        }
    }

    @Override
    public Long getClapInnovation(Long id) {
        return clapRepository.countAllClapByInnovationId(id);
    }

    @Override
    public CommentInnovation createComment(String content, Long innovationId) {
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Innovation inno = this.innovationRepository.findById(innovationId).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any innovation"));
        Account account = this.accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        CommentInnovation c = new CommentInnovation();
        c.setContent(content);
        c.setInnovation(inno);
        c.setAccount(account);
        c.setStatus(StatusType.ACTIVE);
        c.setCreatedAt(LocalDateTime.now());
        c.setLastModifiedAt(LocalDateTime.now());
        commentRepository.save(c);

        return c;
    }

    @Override
    public boolean deleteAnComment(Long id) {
        CommentInnovation c = commentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any comment"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if(c.getAccount().getId() == principal.getAccountId() || a.getRole().getName().equals(RoleType.ADMIN.toString())) {
            c.setStatus(StatusType.DELETED);
            commentRepository.save(c);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAnComment(Long id, String content) {
        CommentInnovation c = commentRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any comment"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(c.getAccount().getId() == principal.getAccountId()) {
            c.setContent(content);
            c.setLastModifiedAt(LocalDateTime.now());
            commentRepository.save(c);
            return true;
        }
        return false;
    }

    @Override
    public List<CommentResponse> getAllCommentByInnovationId(Long id) {
        List<CommentInnovation> commentList = commentRepository.getAllCommentByInnovationId(id);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (CommentInnovation ci : commentList) {
            commentResponseList.add(new CommentResponse(ci.getId(), ci.getContent(), ci.getAccount().getUsername(), ci.getCreatedAt(), ci.getLastModifiedAt()));
        }

        return commentResponseList;
    }

    private Specification<Innovation> createTheCombiningInnovationSpecification(InnovationFilter innovationFilter, Optional<String> keyword) {

        Specification<Innovation> combinedSpecification = Specification.where(null);
        if (!keyword.isEmpty()) {
            combinedSpecification = combinedSpecification.and(new InnovationSpecification(new QueryCriteria("content", keyword.get())));
        }
        if (innovationFilter.isOwn()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository
                    .findByUsernameIgnoreCase(userDetails.getUsername())
                    .orElseThrow(RecordNotFoundException::new);
            combinedSpecification = combinedSpecification.and(new InnovationSpecification(new QueryCriteria("accountId", account.getId())));
        }
        combinedSpecification = combinedSpecification.and(this.getProjectNameSpecifications(innovationFilter));
        combinedSpecification = combinedSpecification.and(this.getAreasSpecifications(innovationFilter));
        combinedSpecification = combinedSpecification.and(this.getTypeSpecifications(innovationFilter));
        combinedSpecification = combinedSpecification.and(this.getStatusSpecifications(innovationFilter));
        return combinedSpecification;
    }

    private Specification<Innovation> getStatusSpecifications(InnovationFilter innovationFilter) {
        Specification<Innovation> typeSpecification = Specification.where(null);
        Optional<List<Status>> filterStatuses = innovationFilter.getStatuses();
        if (!filterStatuses.isEmpty()) {
            for (Status status : filterStatuses.get()) {
                typeSpecification = typeSpecification.or(new InnovationSpecification(new QueryCriteria("status", status)));
            }
        }
        return typeSpecification;
    }

    private Specification<Innovation> getTypeSpecifications(InnovationFilter innovationFilter) {
        Specification<Innovation> typeSpecification = Specification.where(null);
        Optional<List<Long>> filterTypes = innovationFilter.getTypes();
        if (!filterTypes.isEmpty()) {
            for (Long typeId : filterTypes.get()) {
                typeSpecification = typeSpecification.or(new InnovationSpecification(new QueryCriteria("typeId", typeId)));
            }
        }
        return typeSpecification;
    }

    private Specification<Innovation> getProjectNameSpecifications(InnovationFilter innovationFilter) {
        Specification<Innovation> projectNameSpecification = Specification.where(null);
        Optional<List<String>> filterProjects = innovationFilter.getProjects();
        if (!filterProjects.isEmpty()) {
            for (String projectName : filterProjects.get()) {
                projectNameSpecification = projectNameSpecification.or(new InnovationSpecification(new QueryCriteria("projectName", projectName)));
            }
        }
        return projectNameSpecification;
    }

    private Specification<Innovation> getAreasSpecifications(InnovationFilter innovationFilter) {
        Specification<Innovation> areaSpecification = Specification.where(null);
        Optional<List<Long>> filterAreas = innovationFilter.getAreas();
        if (!filterAreas.isEmpty()) {
            for (Long areaId : filterAreas.get()) {
                areaSpecification = areaSpecification.or(new InnovationSpecification(new QueryCriteria("areas", areaId)));
            }
        }
        return areaSpecification;
    }

    @Transactional(rollbackFor = {AmazonServiceException.class, DataIntegrityViolationException.class, NonTransientDataAccessException.class})
    public void deleteOldAttachmentsAndSaveNewAttachments(Innovation savedInnovation, InnovationUpdatingRequest request) {
        List<AttachmentDTO> requestAttachments = request.getAttachmentList();
        List<AttachmentDTO> savedAttachments = this.attachmentRepository.findAllByInnovationId(savedInnovation.getId())
                .stream()
                .map(attachment -> this.mapper.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());

        List<AttachmentDTO> deletingAttachment = new LinkedList<>(savedAttachments);
        deletingAttachment.removeAll(requestAttachments);
        deletingAttachment.forEach(attachmentDTO -> {
            this.awss3Service.deleteFileFromS3Bucket(attachmentDTO.getUniqueName());
            this.attachmentRepository.removeByUniqueNameEquals(attachmentDTO.getUniqueName());
        });

        requestAttachments.removeAll(savedAttachments);
        List<Attachment> newAttachments = requestAttachments.stream().map(attachmentDTO -> {
            Attachment attachment = this.mapper.map(attachmentDTO, Attachment.class);
            attachment.setInnovation(savedInnovation);
            return attachment;
        }).collect(Collectors.toList());
        this.attachmentRepository.saveAll(newAttachments);
    }

    private List<Area> getListArea(List<Long> listAreaId) {
        return listAreaId
                .stream()
                .map(id -> areaRepository
                        .findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("The provided area is not found")))
                .collect(Collectors.toList());
    }
}

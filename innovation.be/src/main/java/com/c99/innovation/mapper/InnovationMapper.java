package com.c99.innovation.mapper;

import com.c99.innovation.common.AuditableHelper;
import com.c99.innovation.common.enumtype.InnovationType;
import com.c99.innovation.dto.AttachmentDTO;
import com.c99.innovation.dto.request.InnovationCreationRequest;
import com.c99.innovation.dto.request.InnovationUpdatingRequest;
import com.c99.innovation.dto.response.InnovationDetailResponse;
import com.c99.innovation.dto.sortingfiltering.PageDTO;
import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.Attachment;
import com.c99.innovation.entity.AuditableEntity;
import com.c99.innovation.entity.Innovation;
import com.c99.innovation.entity.Team;
import com.c99.innovation.entity.Type;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.AttachmentRepository;
import com.c99.innovation.repository.InnovationAreaRepository;
import com.c99.innovation.repository.InnovationRepository;
import com.c99.innovation.repository.TeamRepository;
import com.c99.innovation.repository.TypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InnovationMapper {

    private ModelMapper mapper;

    private TypeRepository typeRepository;

    private TeamRepository teamRepository;

    private AuditableHelper auditableHelper;

    private AttachmentRepository attachmentRepository;

    private InnovationRepository innovationRepository;

    private InnovationAreaRepository innovationAreaRepository;

    @Autowired
    public InnovationMapper(ModelMapper mapper,
                            TypeRepository typeRepository,
                            TeamRepository teamRepository,
                            AuditableHelper auditableHelper,
                            AttachmentRepository attachmentRepository,
                            InnovationRepository innovationRepository,
                            InnovationAreaRepository innovationAreaRepository) {
        this.mapper = mapper;
        this.typeRepository = typeRepository;
        this.teamRepository = teamRepository;
        this.auditableHelper = auditableHelper;
        this.attachmentRepository = attachmentRepository;
        this.innovationRepository = innovationRepository;
        this.innovationAreaRepository = innovationAreaRepository;
    }

    public Innovation convertToEntity(InnovationCreationRequest innovationCreationRequest) {
        innovationCreationRequest = processHidingResultFieldsIfRequestIsIdea(innovationCreationRequest);
        Innovation innovation = this.mapper.map(innovationCreationRequest, Innovation.class);

        Type type = typeRepository.findById(innovationCreationRequest.getTypeId())
                .orElseThrow(() -> new RecordNotFoundException("The provided type is not existed"));
        Team team = teamRepository.findById(innovationCreationRequest.getTeamId())
                .orElseThrow(() -> new RecordNotFoundException("The provided team is not existed"));
        innovation.setType(type);
        innovation.setTeam(team);

        AuditableEntity auditableEntity = auditableHelper.updateAuditingCreatedFields(innovation);
        innovation = (Innovation) auditableHelper.updateAuditingModifiedFields(auditableEntity);
        return innovation;
    }

    public Innovation convertToEntity(Long id, InnovationUpdatingRequest request) {
        request = (InnovationUpdatingRequest) processHidingResultFieldsIfRequestIsIdea(request);
        Innovation entity = this.innovationRepository.findActiveInnovationById(id).orElseThrow(() -> new RecordNotFoundException("Not contain this entity to update"));
        this.mapper.map(request, entity);
        entity.setId(id);
        Type type = typeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new RecordNotFoundException("The provided type is not existed"));
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new RecordNotFoundException("The provided team is not existed"));
        entity.setType(type);
        entity.setTeam(team);
        entity = (Innovation) auditableHelper.updateAuditingModifiedFields(entity);
        return entity;
    }

    public InnovationDetailResponse convertToDto(Innovation innovation, List<Area> listArea) {
        InnovationDetailResponse innovationResponse = this.mapper.map(innovation, InnovationDetailResponse.class);
        innovationResponse.setAreaList(listArea);
        innovationResponse = (InnovationDetailResponse) this.auditableHelper.setupDisplayAuditableRelatedToTimestampFields(innovation, innovationResponse);
        innovationResponse = (InnovationDetailResponse) this.auditableHelper.setupDisplayAuditableRelatedToPersonFields(innovation, innovationResponse);

        List<Attachment> attachmentList = this.attachmentRepository.findAllByInnovationId(innovation.getId());
        List<AttachmentDTO> attachmentDTOList = attachmentList.stream().map(attachment -> mapper.map(attachment, AttachmentDTO.class)).collect(Collectors.toList());
        innovationResponse.setAttachmentList(attachmentDTOList);

        return innovationResponse;
    }

    public InnovationCreationRequest processHidingResultFieldsIfRequestIsIdea(InnovationCreationRequest innovationCreationRequest) {
        Long requestTypeId = innovationCreationRequest.getTypeId();
        Type type = this.typeRepository.findById(requestTypeId).orElseThrow(() -> new RecordNotFoundException("Not contain this type"));
        if (InnovationType.IDEA.toString().equalsIgnoreCase(type.getName())) {
            innovationCreationRequest.setProposedSolution(null);
            innovationCreationRequest.setResult(null);
        }
        return innovationCreationRequest;
    }

    public List<InnovationDetailResponse> toDTOs(List<Innovation> oldContent) {
        List<InnovationDetailResponse> list = new ArrayList<>();
        for (Innovation innovation : oldContent) {
            List<Area> areas = this.innovationAreaRepository.findAllAreaByInnovationId(innovation.getId());
            list.add(this.convertToDto(innovation, areas));
        }
        return list;
    }

    public PageDTO<InnovationDetailResponse> toPageDTO(Page<Innovation> inPage) {

        List<Innovation> oldContent = inPage.getContent();
        List<InnovationDetailResponse> newContent = this.toDTOs(oldContent);

        PageDTO<InnovationDetailResponse> outPage = new PageDTO<>();
        outPage.setContent(newContent);
        outPage.setSize(inPage.getSize());
        outPage.setTotalPages(inPage.getTotalPages());
        outPage.setCurrentPage(inPage.getNumber());
        outPage.setTotalElements(inPage.getTotalElements());
        outPage.setNumberOfElement(inPage.getNumberOfElements());
        outPage.setFirst(outPage.getCurrentPage() == 0);
        outPage.setLast(outPage.getCurrentPage() >= inPage.getTotalPages() - 1);
        outPage.setEmpty(inPage.getTotalElements() == 0);
        return outPage;
    }
}

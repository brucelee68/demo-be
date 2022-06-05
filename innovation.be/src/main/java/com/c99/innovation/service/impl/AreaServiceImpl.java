package com.c99.innovation.service.impl;

import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.*;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.repository.AreaRepository;
import com.c99.innovation.repository.InnovationAreaRepository;
import com.c99.innovation.repository.InnovationRepository;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.InnovationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.c99.innovation.common.constant.Constants.*;

@Service
public class AreaServiceImpl implements com.c99.innovation.service.AreaService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InnovationRepository innovationRepository;

    @Autowired
    private InnovationAreaRepository innovationAreaRepository;

    @Autowired
    private InnovationService innovationService;

    private AreaRepository areaRepository;

    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public List<Area> getAll() {
        return this.areaRepository.getAllActiveArea();
    }

    @Override
    public Map<String, Object> getAllActiveArea(int page, int size) {
        Page<Area> areaManagement = areaRepository.findAllByStatus(StatusType.ACTIVE,PageRequest.of(page, size));
        List<Area> areaList = areaManagement.getContent();
        int start = size * (page+1) - size + 1;
        Map<String, Object> response = new HashMap<>();
        response.put("areas", areaList);
        response.put("currentPage", areaManagement.getNumber());
        response.put("size", areaManagement.getSize());
        response.put("totalElements",  areaManagement.getTotalElements());
        response.put("totalPages",  areaManagement.getTotalPages());
        response.put("startPage", start);
        return response;
    }

    @Override
    public boolean deleteArea(Long id) {
        Area area = areaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any area"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if(a.getRole().getName().equals(RoleType.ADMIN.toString())){
            area.setStatus(StatusType.DELETED);
            areaRepository.save(area).getStatus();

            List<InnovationArea> innovationAreaList = innovationAreaRepository.findAll();
            for (InnovationArea ia : innovationAreaList) {
                if(ia.getArea().getId() == id){
                    innovationService.deleteInnovation(ia.getInnovation().getId());
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public String createArea(String area) {
        area = area.substring(0, 1).toUpperCase() + area.toLowerCase().substring(1);
        boolean isExistedArea = areaRepository.existsByName(area);
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if(isExistedArea){
            return AREA_EXISTED;
        }
        if(a.getRole().getName().equals(RoleType.ADMIN.toString())){
            Area newArea = new Area();
            newArea.setName(area);
            newArea.setStatus(StatusType.ACTIVE);
            areaRepository.save(newArea);
            return CREATE_SUCCESSFULLY;
        }
        return CREATE_FAILED;
    }

    @Override
    public String updateArea(Long id, String newName) {
        newName = newName.substring(0, 1).toUpperCase() + newName.toLowerCase().substring(1);
        boolean isExistedArea = areaRepository.existsByName(newName);
        Area area = areaRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any area"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if(isExistedArea){
            return AREA_EXISTED;
        }
        if(a.getRole().getName().equals(RoleType.ADMIN.toString())){
            area.setName(newName);
            areaRepository.save(area);
            return UPDATE_SUCCESSFULLY;
        }
        return UPDATE_FAILED;
    }
}
package com.c99.innovation.service.impl;

import com.c99.innovation.common.enumtype.RoleType;
import com.c99.innovation.common.enumtype.StatusType;
import com.c99.innovation.entity.Account;
import com.c99.innovation.entity.Innovation;
import com.c99.innovation.entity.InnovationArea;
import com.c99.innovation.entity.Team;
import com.c99.innovation.exception.RecordNotFoundException;
import com.c99.innovation.repository.AccountRepository;
import com.c99.innovation.repository.InnovationRepository;
import com.c99.innovation.repository.TeamRepository;
import com.c99.innovation.security.UserDetailImpl;
import com.c99.innovation.service.InnovationService;
import com.c99.innovation.service.TeamService;
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
public class TeamServiceImpl implements TeamService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InnovationRepository innovationRepository;

    @Autowired
    private InnovationService innovationService;

    private TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Team> getAll() {
        return this.teamRepository.getAllActiveTeam();
    }

    @Override
    public Map<String, Object> getAllActiveTeam(int page, int size) {
        Page<Team> teamManagement = teamRepository.findAllByStatus(StatusType.ACTIVE,PageRequest.of(page, size));
        List<Team> teamList = teamManagement.getContent();
        int start = size * (page+1) - size + 1;
        Map<String, Object> response = new HashMap<>();
        response.put("teams", teamList);
        response.put("currentPage", teamManagement.getNumber());
        response.put("size", teamManagement.getSize());
        response.put("totalElements",  teamManagement.getTotalElements());
        response.put("totalPages",  teamManagement.getTotalPages());
        response.put("startPage", start);
        return response;
    }

    @Override
    public String deleteTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any team"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if(a.getRole().getName().equals(RoleType.ADMIN.toString())){
            team.setStatus(StatusType.DELETED);
            teamRepository.save(team);

            List<Innovation> innovationList = innovationRepository.findAll();
            for (Innovation i : innovationList) {
                if(i.getTeam().getId() == id){
                    innovationService.deleteInnovation(i.getId());
                }
            }

            return DELETE_SUCCESSFULLY;
        }
        return DELETE_FAILED;
    }

    @Override
    public String editTeam(Long id, String newTeamName, String newProjectName) {
        List<Team> listTeam;
        listTeam = teamRepository.findAll();
        for (int i = 0; i < listTeam.size(); i++) {
            if (newTeamName.equalsIgnoreCase(listTeam.get(i).getName()) && newProjectName.equalsIgnoreCase(listTeam.get(i).getProjectName())) {
                return TEAM_EXISTED;
            }
        }
        Team t = teamRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any team"));
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));
        if (a.getRole().getName().equals(RoleType.ADMIN.toString())) {
            t.setName(newTeamName);
            t.setProjectName(newProjectName);
            teamRepository.save(t);
            return UPDATE_SUCCESSFULLY;
        }
        return UPDATE_FAILED;
    }

    @Override
    public String createTeam(String team, String project) {
        List<Team> listTeam;
        listTeam = teamRepository.findAll();
        for(int i = 0; i < listTeam.size(); i++){
            if(team.equalsIgnoreCase(listTeam.get(i).getName()) && project.equalsIgnoreCase(listTeam.get(i).getProjectName())){
                return TEAM_EXISTED;
            }
        }
        UserDetailImpl principal = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account a = accountRepository.findById(principal.getAccountId()).orElseThrow(() -> new RecordNotFoundException("The provided id is not matched with any account"));

        if(a.getRole().getName().equals(RoleType.ADMIN.toString())){
            Team t = new Team();
            t.setName(team);
            t.setProjectName(project);
            t.setStatus(StatusType.ACTIVE);
            teamRepository.save(t);
            return CREATE_SUCCESSFULLY;
        }
        return CREATE_FAILED;
    }


}

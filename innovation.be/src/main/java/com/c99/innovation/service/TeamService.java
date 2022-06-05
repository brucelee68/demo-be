package com.c99.innovation.service;

import com.c99.innovation.entity.Team;

import java.util.List;
import java.util.Map;

public interface TeamService {
    List<Team> getAll();

//    List<Team> getAllActiveTeam();

    String deleteTeam(Long id);

    String editTeam(Long id, String newTeamName, String newProjectName);

    String createTeam(String team, String project);

    Map<String, Object> getAllActiveTeam(int page, int size);
}

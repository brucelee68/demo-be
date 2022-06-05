package com.c99.innovation.dto.request;

import javax.validation.constraints.NotBlank;

public class TeamUpdateRequest {

    @NotBlank
    private String newTeam;

    @NotBlank
    private String newProjectName;

    public String getNewTeam() {
        return newTeam;
    }

    public void setNewTeam(String newTeam) {
        this.newTeam = newTeam;
    }

    public String getNewProjectName() {
        return newProjectName;
    }

    public void setNewProjectName(String newProjectName) {
        this.newProjectName = newProjectName;
    }
}

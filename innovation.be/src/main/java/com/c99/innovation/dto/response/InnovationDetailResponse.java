package com.c99.innovation.dto.response;

import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.dto.AttachmentDTO;
import com.c99.innovation.dto.AuditableResponse;
import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.Team;
import com.c99.innovation.entity.Type;

import java.util.List;

public class InnovationDetailResponse extends AuditableResponse {

    private Long id;

    private String content;

    private String problem;

    private Status status;

    private String objective;

    private String proposedSolution;

    private String result;

    private String scope;

    private Team team;

    private Type type;

    private List<Area> areaList;

    private List<AttachmentDTO> attachmentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getProposedSolution() {
        return proposedSolution;
    }

    public void setProposedSolution(String proposedSolution) {
        this.proposedSolution = proposedSolution;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public List<AttachmentDTO> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentDTO> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

}

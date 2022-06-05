package com.c99.innovation.dto.request;

import com.c99.innovation.dto.AttachmentDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class InnovationCreationRequest {

    @NotBlank
    private String content;

    @NotBlank
    private String problem;

    private String proposedSolution;

    @NotBlank
    private String objective;

    private String result;

    @NotBlank
    private String scope;

    @NotNull
    private Long teamId;

    @NotNull
    private List<Long> listAreaId;

    @NotNull
    private Long typeId;

    private List<AttachmentDTO> attachmentList;

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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public List<Long> getListAreaId() {
        return listAreaId;
    }

    public void setListAreaId(List<Long> listAreaId) {
        this.listAreaId = listAreaId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public List<AttachmentDTO> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<AttachmentDTO> attachmentList) {
        this.attachmentList = attachmentList;
    }
}

package com.c99.innovation.dto.response;

import com.c99.innovation.common.enumtype.Status;

import java.time.LocalDateTime;
import java.util.List;

public class MyInnovationAreaResponse {
    private Long id;
    private String content;
    private String projectName;
    private String typeName;
    private LocalDateTime createdAt;
    private Status status;
    private List<String> area;

    public MyInnovationAreaResponse(Long id, String content, String projectName, String typeName, LocalDateTime createdAt, Status status, List<String> area) {
        this.id = id;
        this.content = content;
        this.projectName = projectName;
        this.typeName = typeName;
        this.createdAt = createdAt;
        this.status = status;
        this.area = area;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}

package com.c99.innovation.dto.response;

import com.c99.innovation.common.enumtype.Status;
import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.Team;
import com.c99.innovation.entity.Type;

import java.time.LocalDateTime;
import java.util.List;

public class MyInnovation {
    private Long id;
    private String content;
    private String projectName;
    private String typeName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifedAt;
    private Status status;

    public MyInnovation(Long id, String content, String projectName, String typeName, LocalDateTime createdAt, LocalDateTime lastModifedAt, Status status) {
        this.id = id;
        this.content = content;
        this.projectName = projectName;
        this.typeName = typeName;
        this.createdAt = createdAt;
        this.lastModifedAt = lastModifedAt;
        this.status = status;
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


    public LocalDateTime getLastModifedAt() {
        return lastModifedAt;
    }

    public void setLastModifedAt(LocalDateTime lastModifedAt) {
        this.lastModifedAt = lastModifedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

package com.c99.innovation.dto;

import com.c99.innovation.dto.response.AccountResponse;

import java.time.LocalDateTime;

public abstract class AuditableResponse {

    private LocalDateTime createdAt;

    private AccountResponse createdBy;

    private LocalDateTime lastModifiedAt;

    private AccountResponse lastModifiedBy;

    private Boolean active;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AccountResponse getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AccountResponse createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public AccountResponse getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(AccountResponse lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

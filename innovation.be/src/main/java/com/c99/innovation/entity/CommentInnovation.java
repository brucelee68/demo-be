package com.c99.innovation.entity;

import com.c99.innovation.common.enumtype.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class CommentInnovation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status")
    private StatusType status;

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    protected LocalDateTime lastModifiedAt;

    @JsonIgnore
    @ManyToOne(targetEntity = Innovation.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "innovation_id")
    private Innovation innovation;

    @JsonIgnore
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public CommentInnovation() {
    }

    public CommentInnovation(String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt, Innovation innovation, Account account) {
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.innovation = innovation;
        this.account = account;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
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

    public Innovation getInnovation() {
        return innovation;
    }

    public void setInnovation(Innovation innovation) {
        this.innovation = innovation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}


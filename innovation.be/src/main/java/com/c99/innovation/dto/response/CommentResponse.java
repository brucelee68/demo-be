package com.c99.innovation.dto.response;

import java.time.LocalDateTime;

public class CommentResponse {

    private Long id;

    private String content;

    private String username;

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

    public CommentResponse() {
    }

    public CommentResponse(Long id, String content, String username, LocalDateTime createAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.createAt = createAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

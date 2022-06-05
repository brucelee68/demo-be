package com.c99.innovation.dto.response;

import java.time.LocalDateTime;

public class LatestCommentsResponse {

    private String content;

    private String username;

    private Long innovationId;

    private String title;

    private LocalDateTime createAt;

    private LocalDateTime lastModifiedAt;

    public LatestCommentsResponse() {
    }

    public LatestCommentsResponse(String content, String username, Long innovationId, String title, LocalDateTime createAt, LocalDateTime lastModifiedAt) {
        this.content = content;
        this.username = username;
        this.innovationId = innovationId;
        this.title = title;
        this.createAt = createAt;
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

    public Long getInnovationId() {
        return innovationId;
    }

    public void setInnovationId(Long innovationId) {
        this.innovationId = innovationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}

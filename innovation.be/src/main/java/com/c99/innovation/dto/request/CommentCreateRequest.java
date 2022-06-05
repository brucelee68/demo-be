package com.c99.innovation.dto.request;

import javax.validation.constraints.NotBlank;

public class CommentCreateRequest {

    @NotBlank
    private String content;

    @NotBlank
    private String innovationId;

    public CommentCreateRequest() {
    }

    public CommentCreateRequest(String content, String innovationId) {
        this.content = content;
        this.innovationId = innovationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInnovationId() {
        return innovationId;
    }

    public void setInnovationId(String innovationId) {
        this.innovationId = innovationId;
    }
}

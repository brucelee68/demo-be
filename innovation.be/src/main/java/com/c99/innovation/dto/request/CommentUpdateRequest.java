package com.c99.innovation.dto.request;

import javax.validation.constraints.NotBlank;

public class CommentUpdateRequest {

    @NotBlank
    private String id;

    @NotBlank
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

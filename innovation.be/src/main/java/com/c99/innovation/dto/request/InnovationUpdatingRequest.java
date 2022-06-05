package com.c99.innovation.dto.request;

public class InnovationUpdatingRequest extends InnovationCreationRequest {

    private Long id;

    public InnovationUpdatingRequest() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

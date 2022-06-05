package com.c99.innovation.dto.request;

import javax.validation.constraints.NotBlank;

public class AreaCreateRequest {
    @NotBlank
    private String newArea;

    public String getNewArea() {
        return newArea;
    }

    public void setNewArea(String newArea) {
        this.newArea = newArea;
    }
}

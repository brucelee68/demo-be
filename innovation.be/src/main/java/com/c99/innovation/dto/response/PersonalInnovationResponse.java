package com.c99.innovation.dto.response;

import java.util.List;

public class PersonalInnovationResponse {


    private List<MyInnovationAreaResponse> latestOwnInnovations;

    public PersonalInnovationResponse(List<MyInnovationAreaResponse> innovationCurrents) {
        this.latestOwnInnovations = innovationCurrents;
    }

    public List<MyInnovationAreaResponse> getLatestOwnInnovations() {
        return latestOwnInnovations;
    }

    public void setLatestOwnInnovations(List<MyInnovationAreaResponse> latestOwnInnovations) {
        this.latestOwnInnovations = latestOwnInnovations;
    }
}

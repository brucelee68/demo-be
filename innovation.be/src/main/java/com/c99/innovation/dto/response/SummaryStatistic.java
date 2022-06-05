package com.c99.innovation.dto.response;

import java.util.List;

public class SummaryStatistic {

    private TotalCountingInTypes totalCountingInTypes;

    private List<TopInnovation> topInnovations;

    private List<TopContributing> topContributors;

    private List<LatestOwnInnovation> latestOwnInnovations;

    private List<TopInnovationByClap> topInnovationsByClap;
    
    private List<LatestCommentsResponse> latestCommentsResponse;

    public SummaryStatistic(TotalCountingInTypes totalCountingInTypes, List<TopInnovation> topInnovations, List<TopContributing> topContributors, List<LatestOwnInnovation> latestOwnInnovations, List<TopInnovationByClap> topInnovationsByClap, List<LatestCommentsResponse> latestCommentsResponse) {
        this.totalCountingInTypes = totalCountingInTypes;
        this.topInnovations = topInnovations;
        this.topContributors = topContributors;
        this.latestOwnInnovations = latestOwnInnovations;
        this.topInnovationsByClap = topInnovationsByClap;
        this.latestCommentsResponse = latestCommentsResponse;
    }

    public List<LatestCommentsResponse> getLatestCommentsResponse() {
        return latestCommentsResponse;
    }

    public void setLatestCommentsResponse(List<LatestCommentsResponse> latestCommentsResponse) {
        this.latestCommentsResponse = latestCommentsResponse;
    }

    public TotalCountingInTypes getTotalCountingInTypes() {
        return totalCountingInTypes;
    }

    public void setTotalCountingInTypes(TotalCountingInTypes totalCountingInTypes) {
        this.totalCountingInTypes = totalCountingInTypes;
    }

    public List<TopInnovation> getTopInnovations() {
        return topInnovations;
    }

    public void setTopInnovations(List<TopInnovation> topInnovations) {
        this.topInnovations = topInnovations;
    }

    public List<TopContributing> getTopContributors() {
        return topContributors;
    }

    public void setTopContributors(List<TopContributing> topContributors) {
        this.topContributors = topContributors;
    }

    public List<LatestOwnInnovation> getLatestOwnInnovations() {
        return latestOwnInnovations;
    }

    public void setLatestOwnInnovations(List<LatestOwnInnovation> latestOwnInnovations) {
        this.latestOwnInnovations = latestOwnInnovations;
    }

    public List<TopInnovationByClap> getTopInnovationsByClap() {
        return topInnovationsByClap;
    }

    public void setTopInnovationsByClap(List<TopInnovationByClap> topInnovationsByClap) {
        this.topInnovationsByClap = topInnovationsByClap;
    }
}

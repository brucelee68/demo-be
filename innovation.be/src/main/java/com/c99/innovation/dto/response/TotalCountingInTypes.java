package com.c99.innovation.dto.response;

public class TotalCountingInTypes {

    private long innovation;

    private long idea;

    private long improvement;
    
    private long total;

    public TotalCountingInTypes(long innovation, long idea, long improvement, long total) {
        this.innovation = innovation;
        this.idea = idea;
        this.improvement = improvement;
        this.total = total;
    }

    public long getInnovation() {
        return innovation;
    }

    public void setInnovation(long innovation) {
        this.innovation = innovation;
    }

    public long getIdea() {
        return idea;
    }

    public void setIdea(long idea) {
        this.idea = idea;
    }

    public long getImprovement() {
        return improvement;
    }

    public void setImprovement(long improvement) {
        this.improvement = improvement;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}

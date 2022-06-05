package com.c99.innovation.dto.response;

public class TypesCountingInCertainTime {

    private int time;
    private int year;
    private long innovation;
    private long improvement;
    private long idea;

    public TypesCountingInCertainTime(int time, int year) {
        this.time = time;
        this.year = year;
        this.improvement = 0;
        this.innovation = 0;
        this.idea = 0;
    }

    public TypesCountingInCertainTime(int time, int year, long innovation, long improvement, long idea) {
        this.time = time;
        this.year = year;
        this.innovation = innovation;
        this.improvement = improvement;
        this.idea = idea;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getInnovation() {
        return innovation;
    }

    public void setInnovation(long innovation) {
        this.innovation = innovation;
    }

    public long getImprovement() {
        return improvement;
    }

    public void setImprovement(long improvement) {
        this.improvement = improvement;
    }

    public long getIdea() {
        return idea;
    }

    public void setIdea(long idea) {
        this.idea = idea;
    }
}

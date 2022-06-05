package com.c99.innovation.dto.response;

public class ContributingCounting {

    private String name;

    private long count;

    public ContributingCounting() {
    }

    public ContributingCounting(String projectName, long count) {
        this.name = projectName;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

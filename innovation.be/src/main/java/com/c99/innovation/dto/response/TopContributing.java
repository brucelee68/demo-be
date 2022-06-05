package com.c99.innovation.dto.response;

public class TopContributing {

    private String name;

    private long count;

    public TopContributing() {

    }

    public TopContributing(String username, long total) {
        this.name = username;
        this.count = total;
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

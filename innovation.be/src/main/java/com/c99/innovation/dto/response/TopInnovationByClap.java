package com.c99.innovation.dto.response;

import com.c99.innovation.entity.Type;

public class TopInnovationByClap {

    private long innovationID;
    private String content;
    private long clap;
    private Type type;

    public TopInnovationByClap(long innovationID, String content, long clap, Type type) {
        this.innovationID = innovationID;
        this.content = content;
        this.clap = clap;
        this.type = type;
    }

    public long getInnovationID() {
        return innovationID;
    }

    public void setInnovationID(long innovationID) {
        this.innovationID = innovationID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getClap() {
        return clap;
    }

    public void setClap(long clap) {
        this.clap = clap;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

package com.c99.innovation.dto.response;

import com.c99.innovation.entity.Area;
import com.c99.innovation.entity.Team;
import com.c99.innovation.entity.Type;

import java.time.LocalDateTime;
import java.util.List;

public class TopInnovation {

    private Long id;
    private String content;
    private Team team;
    private List<Area> areas;
    private Type type;
    private LocalDateTime createdAt;

    public TopInnovation(Long id, String content, Team team, Type type, List<Area> areas, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.team = team;
        this.areas = areas;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

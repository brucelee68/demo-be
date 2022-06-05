package com.c99.innovation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "innovation_area")
public class InnovationArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(targetEntity = Innovation.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "innovation_id")
    private Innovation innovation;

    @JsonIgnore
    @ManyToOne(targetEntity = Area.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id")
    private Area area;

    public InnovationArea() {

    }

    public InnovationArea(Innovation innovation, Area area) {
        this.innovation = innovation;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Innovation getInnovation() {
        return innovation;
    }

    public void setInnovation(Innovation innovation) {
        this.innovation = innovation;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}

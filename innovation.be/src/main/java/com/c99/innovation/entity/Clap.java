package com.c99.innovation.entity;

import com.c99.innovation.common.enumtype.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Optional;


@Entity
@Table(name = "clap")
public class Clap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clap", columnDefinition = "integer default 0")
    private Long clap ;

    @Column(name = "status")
    private StatusType status;

    @JsonIgnore
    @ManyToOne(targetEntity = Innovation.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "innovation_id")
    private Innovation innovation;

    @JsonIgnore
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Clap() {
    }

    public Clap(Long clap, StatusType status, Innovation innovation, Account account) {
        this.clap = clap;
        this.status = status;
        this.innovation = innovation;
        this.account = account;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Long getClap() {
        return clap;
    }

    public void setClap(Long clap) {
        this.clap = clap;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

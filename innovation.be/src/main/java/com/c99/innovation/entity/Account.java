package com.c99.innovation.entity;

import com.c99.innovation.common.enumtype.StatusType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "status")
    private StatusType status;

    @ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = true, name = "refresh_token", unique = true)
    private String refreshToken;

    @Column(nullable = true, name = "token_expired_date")
    private Instant tokenExpiredDate;

    public Account() {

    }

    public Account(Long id, String username, String roleName) {
        this.id = id;
        this.username = username;
        Role temporaryRole = new Role();
        temporaryRole.setName(roleName);
        this.role = temporaryRole;
    }

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public Account setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Account setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public Account setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Account setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Instant getTokenExpiredDate() {
        return tokenExpiredDate;
    }

    public Account setTokenExpiredDate(Instant tokenExpiredDate) {
        this.tokenExpiredDate = tokenExpiredDate;
        return this;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}

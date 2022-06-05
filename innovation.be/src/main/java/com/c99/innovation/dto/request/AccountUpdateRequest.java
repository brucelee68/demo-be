package com.c99.innovation.dto.request;

import javax.validation.constraints.NotBlank;

public class AccountUpdateRequest {

    private String newPassword;

    @NotBlank
    private String roleId;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

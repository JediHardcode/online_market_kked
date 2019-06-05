package com.gmail.derynem.service.model.role;

import javax.validation.constraints.NotNull;

public class UpdateRoleDTO {
    @NotNull
    private Long roleId;
    @NotNull
    private Long id;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
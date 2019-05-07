package com.gmail.derynem.service.model;

import javax.validation.constraints.NotNull;

public class UpdateRoleDTO {
    @NotNull
    private String role;
    @NotNull
    private Long id;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

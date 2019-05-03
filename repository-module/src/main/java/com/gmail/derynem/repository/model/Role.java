package com.gmail.derynem.repository.model;

import com.gmail.derynem.repository.model.enums.RoleEnum;

public class Role {
    private Long id;
    private RoleEnum role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}

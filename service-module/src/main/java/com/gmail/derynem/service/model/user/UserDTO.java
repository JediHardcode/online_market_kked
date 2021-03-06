package com.gmail.derynem.service.model.user;

import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.model.role.RoleDTO;

import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    private Long id;
    private String name;
    private String surName;
    private String middleName;
    private String email;
    private String password;
    private RoleDTO role;
    private boolean deleted;
    private ProfileDTO profile;

    public UserDTO() {
        deleted = false;
    }

    public UserDTO(Long id, String name, String surName, String middleName, String email, String password, RoleDTO role) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
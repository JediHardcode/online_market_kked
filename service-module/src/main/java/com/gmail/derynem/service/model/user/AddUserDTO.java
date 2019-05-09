package com.gmail.derynem.service.model.user;

import com.gmail.derynem.service.model.role.RoleDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.*;

public class AddUserDTO {
    @NotNull
    @Size(max = NAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN)
    private String name;
    @NotNull
    @Size(max = SURNAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN)
    private String surName;
    @NotNull
    @Size(max = MIDDLE_NAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN)
    private String middleName;
    @NotNull
    @Size(max = EMAIL_LENGTH)
    @Pattern(regexp = EMAIL_PATTERN)
    private String email;
    @NotNull
    private Long roleId;
    private Boolean deleted;

    public AddUserDTO() {
        deleted = false;
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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddUserDTO that = (AddUserDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(surName, that.surName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(roleId, that.roleId) &&
                Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surName, middleName, email, roleId, deleted);
    }
}

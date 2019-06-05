package com.gmail.derynem.service.model.user;

import com.gmail.derynem.service.model.profile.ProfileDTO;
import com.gmail.derynem.service.validation.annotation.UniqueEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.*;

public class
AddUserDTO {
    @NotNull(message = "{user.name.empty}")
    @NotEmpty(message = "{user.name.empty}")
    @Size(max = NAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN, message = "{user.name.not.valid}")
    private String name;
    @NotNull(message = "{user.surname.empty}")
    @NotEmpty(message = "{user.surname.empty}")
    @Size(max = SURNAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN, message = "{user.surname.not.valid}")
    private String surName;
    @NotNull(message = "{user.middle.name.empty}")
    @NotEmpty(message = "{user.middle.name.empty}")
    @Size(max = MIDDLE_NAME_LENGTH)
    @Pattern(regexp = ONLY_ENG_LETTER_PATTERN, message = "{user.middle.name.not.valid}")
    private String middleName;
    @NotNull(message = "{user.email.empty}")
    @NotEmpty(message = "{user.email.empty}")
    @Size(max = EMAIL_LENGTH)
    @Pattern(regexp = EMAIL_PATTERN, message = "{user.email.not.valid}")
    @UniqueEmail
    private String email;
    @NotNull
    private Long roleId;
    private boolean deleted;
    private ProfileDTO profile;

    public AddUserDTO() {
        profile = new ProfileDTO();
        deleted = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
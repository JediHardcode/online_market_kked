package com.gmail.derynem.service.model.profile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ENG_LETTER_WITH_DIGITS;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_DIGITS;

public class ProfileDTO {
    private Long id;
    @Pattern(regexp = ENG_LETTER_WITH_DIGITS, message = "{profile.address.not.valid}")
    @NotNull(message = "{profile.address.empty}")
    @NotEmpty(message = "{profile.address.empty}")
    private String address;
    @NotNull(message = "{profile.telephone.empty}")
    @NotEmpty(message = "{profile.telephone.empty}")
    @Pattern(regexp = ONLY_DIGITS, message = "{profile.telephone.not.valid}")
    private String telephone;

    public ProfileDTO() {
        address = " ";
        telephone = " ";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
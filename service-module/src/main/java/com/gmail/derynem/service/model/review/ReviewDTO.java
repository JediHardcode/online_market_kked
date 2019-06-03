package com.gmail.derynem.service.model.review;

import com.gmail.derynem.service.model.user.UserCommonDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ENG_LETTER_WITH_DIGITS;

public class ReviewDTO {
    private Long id;
    private UserCommonDTO user;
    private String created;
    @NotNull
    @NotEmpty
    @Pattern(regexp = ENG_LETTER_WITH_DIGITS, message = "{review.description.not.valid}")
    private String description;
    private boolean hidden;

    public ReviewDTO() {
        created = String.valueOf(LocalDateTime.now());
        hidden = false;
        user = new UserCommonDTO();
    }

    public ReviewDTO(Long id, boolean hidden) {
        this.id = id;
        this.hidden = hidden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCommonDTO getUser() {
        return user;
    }

    public void setUser(UserCommonDTO user) {
        this.user = user;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
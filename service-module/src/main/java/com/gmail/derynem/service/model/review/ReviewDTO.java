package com.gmail.derynem.service.model.review;

import com.gmail.derynem.service.model.user.UserCommonDTO;

public class ReviewDTO {
    private Long id;
    private UserCommonDTO user;
    private String created;
    private String description;
    private boolean hidden;

    public ReviewDTO() {
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
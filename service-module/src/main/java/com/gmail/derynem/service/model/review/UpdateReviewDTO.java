package com.gmail.derynem.service.model.review;

public class UpdateReviewDTO {
    private long id;
    private Boolean hidden;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
}

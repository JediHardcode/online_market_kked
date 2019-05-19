package com.gmail.derynem.service.model.comment;

import com.gmail.derynem.service.model.user.UserDTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.COMMENT_LENGTH;

public class CommentDTO {
    private Long id;
    private UserDTO user;
    private String created;
    private boolean isDeleted;
    @NotEmpty
    @Size(max = COMMENT_LENGTH)
    private String content;

    public CommentDTO() {
        isDeleted = false;
        created = String.valueOf(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
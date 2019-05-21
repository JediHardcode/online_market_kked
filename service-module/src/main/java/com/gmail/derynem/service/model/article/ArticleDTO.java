package com.gmail.derynem.service.model.article;

import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gmail.derynem.service.validation.constant.ValidationConstant.ARTICLE_CONTENT_LENGTH;
import static com.gmail.derynem.service.validation.constant.ValidationConstant.ONLY_ENG_WITH_SPACE;

public class ArticleDTO {
    private Long id;
    @NotEmpty
    @NotNull
    @Pattern(regexp = ONLY_ENG_WITH_SPACE)
    private String name;
    @NotEmpty
    @NotNull
    @Length(max = ARTICLE_CONTENT_LENGTH)
    private String content;
    @NotNull
    private UserDTO user;
    private String created;
    private boolean isDeleted;
    private List<CommentDTO> comments = new ArrayList<>();
    private String preview;

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public ArticleDTO() {
        isDeleted = false;
        created = String.valueOf(LocalDateTime.now());
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
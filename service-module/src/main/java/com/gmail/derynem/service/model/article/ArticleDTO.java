package com.gmail.derynem.service.model.article;

import com.gmail.derynem.service.model.comment.CommentDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
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
    @NotEmpty(message = "{article.name.empty}")
    @NotNull(message = "{article.name.empty}")
    @Pattern(regexp = ONLY_ENG_WITH_SPACE, message = "{article.name}")
    private String name;
    @NotEmpty(message = "{article.content.empty}")
    @NotNull(message = "{article.content.empty}")
    @Length(max = ARTICLE_CONTENT_LENGTH, message = "{article.content}")
    private String content;
    private UserCommonDTO user;
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
        user = new UserCommonDTO();
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

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}
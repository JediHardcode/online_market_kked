package com.gmail.derynem.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "T_COMMENT")
@SQLDelete(sql = "UPDATE T_COMMENT SET F_DELETED = 1 WHERE F_ID =?")
@Where(clause = "F_DELETED = 0")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user;
    @Column(name = "F_CREATED")
    private String created;
    @Column(name = "F_DELETED")
    private boolean isDeleted;
    @Column(name = "F_CONTENT")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return isDeleted == comment.isDeleted &&
                Objects.equals(id, comment.id) &&
                Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isDeleted, content);
    }
}
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
@Table(name = "T_REVIEW")
@SQLDelete(sql = "UPDATE T_REVIEW SET F_DELETED = 1 WHERE F_ID =? ")
@Where(clause = "F_DELETED = 0")
public class Review implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user;
    @Column(name = "F_HIDDEN")
    private boolean hidden;
    @Column(name = "F_DESCRIPTION")
    private String description;
    @Column(name = "F_CREATED")
    private String created;

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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return hidden == review.hidden &&
                Objects.equals(id, review.id) &&
                Objects.equals(user.getId(), review.user.getId()) &&
                Objects.equals(description, review.description) &&
                Objects.equals(created, review.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId(), hidden, description, created);
    }
}
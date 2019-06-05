package com.gmail.derynem.repository.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "T_USER")
@SQLDelete(sql = "UPDATE T_USER SET F_DELETED = 1 WHERE F_ID =? AND F_INVIOLABLE = 0")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private Long id;
    @Column(name = "F_NAME")
    private String name;
    @Column(name = "F_INVIOLABLE")
    private boolean inviolable;
    @Column(name = "F_SURNAME")
    private String surName;
    @Column(name = "F_MIDDLE_NAME")
    private String middleName;
    @Column(name = "F_EMAIL")
    private String email;
    @Column(name = "F_PASSWORD")
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_ROLE_ID", nullable = false)
    private Role role;
    @Column(name = "F_DELETED")
    private boolean deleted;
    @OneToOne(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Profile profile;
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();

    public User() {
    }

    public User(Long id, String name, String surName, String middleName, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.surName = surName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public boolean isInviolable() {
        return inviolable;
    }

    public void setInviolable(boolean inviolable) {
        this.inviolable = inviolable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return deleted == user.deleted &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surName, user.surName) &&
                Objects.equals(middleName, user.middleName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(role.getId(), user.role.getId()) &&
                Objects.equals(profile.getId(), user.profile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surName, middleName, email, password, role.getId(), deleted, profile.getId());
    }
}
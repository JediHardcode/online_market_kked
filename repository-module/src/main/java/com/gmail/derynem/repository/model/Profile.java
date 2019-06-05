package com.gmail.derynem.repository.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "T_PROFILE")
@SQLDelete(sql = "UPDATE T_PROFILE SET F_DELETED = 1 WHERE F_ID =? AND F_INVIOLABLE = 0")
public class Profile implements Serializable {
    @Id
    @GenericGenerator(
            name = "ProfileGenerator",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "user")
    )
    @GeneratedValue(generator = "ProfileGenerator")
    @Column(unique = true, nullable = false, name = "F_ID")
    private Long id;
    @Column(name = "F_ADDRESS")
    private String address;
    @Column(name = "F_TELEPHONE")
    private String telephone;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) &&
                Objects.equals(address, profile.address) &&
                Objects.equals(telephone, profile.telephone) &&
                Objects.equals(user.getId(), profile.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, telephone, user.getId());
    }
}
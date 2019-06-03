package com.gmail.derynem.repository.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "T_ITEM")
@SQLDelete(sql = "UPDATE T_ITEM SET F_DELETED = 1 WHERE F_ID =?")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private Long id;
    @Column(name = "F_NAME")
    private String name;
    @Column(name = "F_DELETED")
    private boolean isDeleted;
    @Column(name = "F_UNIQUE_CODE")
    private String uniqueCode;
    @Column(name = "F_DESCRIPTION")
    private String description;
    @Column(name = "F_PRICE")
    private BigDecimal price;

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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return isDeleted == item.isDeleted &&
                Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(uniqueCode, item.uniqueCode) &&
                Objects.equals(description, item.description) &&
                Objects.equals(price, item.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isDeleted, uniqueCode, description, price);
    }
}
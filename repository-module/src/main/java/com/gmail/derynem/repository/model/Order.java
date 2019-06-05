package com.gmail.derynem.repository.model;

import org.hibernate.annotations.SQLDelete;

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
@Table(name = "T_ORDER")
@SQLDelete(sql = "UPDATE T_ORDER SET F_STATUS = 'REJECTED' WHERE F_ID =?")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_USER_ID", nullable = false)
    private User user;
    @Column(name = "F_CREATED")
    private String created;
    @Column(name = "F_STATUS")
    private String status;
    @Column(name = "F_NUMBER")
    private Long number;
    @Column(name = "F_QUANTITY")
    private Long quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_ITEM_ID", nullable = false)
    private Item item;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(user.getId(), order.user.getId()) &&
                Objects.equals(created, order.created) &&
                Objects.equals(status, order.status) &&
                Objects.equals(number, order.number) &&
                Objects.equals(quantity, order.quantity) &&
                Objects.equals(item.getId(), order.item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId(), created, status, number, quantity, item.getId());
    }
}
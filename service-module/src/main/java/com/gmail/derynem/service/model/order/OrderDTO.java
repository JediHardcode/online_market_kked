package com.gmail.derynem.service.model.order;

import com.gmail.derynem.service.model.enums.OrderStatus;
import com.gmail.derynem.service.model.item.ItemDTO;
import com.gmail.derynem.service.model.user.UserCommonDTO;
import com.gmail.derynem.service.validation.ValidQuantity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {
    private Long id;
    private UserCommonDTO user;
    @NotNull
    @ValidQuantity
    private String quantity;
    private BigDecimal totalPrice;
    private ItemDTO item;
    private String created;
    private OrderStatus status;
    private Long number;

    public OrderDTO() {
        user = new UserCommonDTO();
        item = new ItemDTO();
        created = String.valueOf(LocalDateTime.now());
        status = OrderStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCommonDTO getUser() {
        return user;
    }

    public void setUser(UserCommonDTO user) {
        this.user = user;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }
}
package com.gmail.derynem.service.converter.impl;

import com.gmail.derynem.repository.model.Item;
import com.gmail.derynem.repository.model.Order;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.converter.user.UserConverterAssembler;
import com.gmail.derynem.service.model.enums.OrderStatus;
import com.gmail.derynem.service.model.item.ItemDTO;
import com.gmail.derynem.service.model.order.OrderDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("orderConverter")
public class OrderConverterImpl implements Converter<OrderDTO, Order> {
    private final Converter<ItemDTO, Item> itemConverter;
    private final UserConverterAssembler userConverterAssembler;

    public OrderConverterImpl(@Qualifier("itemConverter") Converter<ItemDTO, Item> itemConverter,
                              UserConverterAssembler userConverterAssembler) {
        this.itemConverter = itemConverter;
        this.userConverterAssembler = userConverterAssembler;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setQuantity(String.valueOf(order.getQuantity()));
        orderDTO.setItem(itemConverter.toDTO(order.getItem()));
        orderDTO.setUser(userConverterAssembler.getUserCommonConverter().toDTO(order.getUser()));
        orderDTO.setId(order.getId());
        orderDTO.setNumber(order.getNumber());
        orderDTO.setStatus(OrderStatus.valueOf(order.getStatus()));
        return orderDTO;
    }

    @Override
    public Order toEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setQuantity(Long.valueOf(orderDTO.getQuantity()));
        order.setStatus(orderDTO.getStatus().name());
        order.setCreated(orderDTO.getCreated());
        return order;
    }
}
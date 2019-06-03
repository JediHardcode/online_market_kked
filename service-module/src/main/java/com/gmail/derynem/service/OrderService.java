package com.gmail.derynem.service;

import com.gmail.derynem.service.exception.OrderServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.enums.OrderStatus;
import com.gmail.derynem.service.model.order.OrderDTO;
import com.gmail.derynem.service.model.user.UserDTO;

public interface OrderService {
    PageDTO<OrderDTO> getOrderPageInfo(Integer page, Integer limit, UserDTO user);

    OrderDTO getById(Long id) throws OrderServiceException;

    void changeStatus(Long id, OrderStatus status) throws OrderServiceException;

    void save(OrderDTO orderDTO);
}
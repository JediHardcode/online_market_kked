package com.gmail.derynem.repository;

import com.gmail.derynem.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {
    int getCountOfOrders(Long id);

    List<Order> getOrders(int offset, Integer limit, Long id);
}
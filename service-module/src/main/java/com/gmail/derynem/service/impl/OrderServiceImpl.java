package com.gmail.derynem.service.impl;

import com.gmail.derynem.repository.ItemRepository;
import com.gmail.derynem.repository.OrderRepository;
import com.gmail.derynem.repository.UserRepository;
import com.gmail.derynem.repository.model.Item;
import com.gmail.derynem.repository.model.Order;
import com.gmail.derynem.repository.model.User;
import com.gmail.derynem.service.OrderService;
import com.gmail.derynem.service.PageService;
import com.gmail.derynem.service.RandomService;
import com.gmail.derynem.service.converter.Converter;
import com.gmail.derynem.service.exception.OrderServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.enums.OrderStatus;
import com.gmail.derynem.service.model.order.OrderDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final PageService pageService;
    private final Converter<OrderDTO, Order> converter;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RandomService randomService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PageService pageService,
                            @Qualifier("orderConverter") Converter<OrderDTO, Order> converter,
                            UserRepository userRepository,
                            ItemRepository itemRepository, RandomService randomService) {
        this.orderRepository = orderRepository;
        this.pageService = pageService;
        this.converter = converter;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.randomService = randomService;
    }


    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrderPageInfo(Integer page, Integer limit, UserDTO user) {
        int validLimit = pageService.validateLimit(limit);
        int countOfOrders;
        if (user == null) {
            countOfOrders = orderRepository.getCountOfOrders(null);
        } else {
            countOfOrders = orderRepository.getCountOfOrders(user.getId());
        }
        int countOfPages = pageService.getPages(countOfOrders, validLimit);
        int offset = pageService.getOffset(page, countOfPages, validLimit);
        List<Order> orders;
        if (user == null) {
            orders = orderRepository.getOrders(offset, validLimit, null);
        } else {
            orders = orderRepository.getOrders(offset, validLimit, user.getId());
        }
        List<OrderDTO> dtoList = orders.stream()
                .map(converter::toDTO)
                .peek(orderDTO -> orderDTO.setTotalPrice
                        (countPrice(Integer.valueOf(orderDTO.getQuantity()), orderDTO.getItem().getPrice())))
                .collect(Collectors.toList());
        PageDTO<OrderDTO> ordersPageInfo = new PageDTO<>();
        ordersPageInfo.setCountOfPages(countOfPages);
        ordersPageInfo.setEntities(dtoList);
        logger.info("Orders info page created, count of pages :{}, count of orders: {}",
                ordersPageInfo.getCountOfPages(), ordersPageInfo.getEntities().size());
        ordersPageInfo.setPage(page);
        ordersPageInfo.setLimit(validLimit);
        return ordersPageInfo;
    }

    @Override
    @Transactional
    public OrderDTO getById(Long id) throws OrderServiceException {
        Order order = orderRepository.getById(id);
        if (order == null) {
            logger.info("order with {} doesnt exist in database", id);
            throw new OrderServiceException("order doesnt exist");
        }
        OrderDTO orderDTO = converter.toDTO(order);
        orderDTO.setTotalPrice(countPrice(Integer.valueOf(orderDTO.getQuantity()), orderDTO.getItem().getPrice()));
        logger.info("order found, user name: {}, item name: {}",
                orderDTO.getUser().getName(), orderDTO.getItem().getName());
        return orderDTO;
    }

    @Override
    @Transactional
    public void changeStatus(Long id, OrderStatus status) throws OrderServiceException {
        Order order = orderRepository.getById(id);
        if (order == null) {
            logger.info("order with {} doesnt exist in database", id);
            throw new OrderServiceException("order doesnt exist");
        }
        order.setStatus(status.name());
        orderRepository.persist(order);
        logger.info(" order with id {} change status to {}", id, status.name());
    }

    @Override
    @Transactional
    public void save(OrderDTO orderDTO) {
        Order order = converter.toEntity(orderDTO);
        order.setNumber(Long.valueOf(randomService.generateUniqueNum()));
        User user = userRepository.getById(orderDTO.getUser().getId());
        order.setUser(user);
        Item item = itemRepository.getById(orderDTO.getItem().getId());
        order.setItem(item);
        orderRepository.persist(order);
        logger.info("order saved, user name: {}, item name: {}", order.getUser().getName(), order.getItem().getName());
    }

    private BigDecimal countPrice(Integer quantity, String price) {
        BigDecimal itemPrice = new BigDecimal(price);
        return itemPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
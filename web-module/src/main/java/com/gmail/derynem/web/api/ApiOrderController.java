package com.gmail.derynem.web.api;

import com.gmail.derynem.service.OrderService;
import com.gmail.derynem.service.exception.OrderServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.order.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;

@RestController
@RequestMapping("/api/v1")
public class ApiOrderController {
    private final static Logger logger = LoggerFactory.getLogger(ApiOrderController.class);
    private final OrderService orderService;

    public ApiOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<PageDTO<OrderDTO>> getListOfItems(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit) {
        PageDTO<OrderDTO> pageDTO = orderService.getOrderPageInfo(page, limit, null);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getArticleWithId(@PathVariable(name = "id") Long id) {

        try {
            OrderDTO orderDTO = orderService.getById(id);
            return new ResponseEntity<>(orderDTO, HttpStatus.OK);
        } catch (OrderServiceException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
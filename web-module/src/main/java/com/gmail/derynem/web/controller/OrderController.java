package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.ItemService;
import com.gmail.derynem.service.OrderService;
import com.gmail.derynem.service.exception.OrderServiceException;
import com.gmail.derynem.service.model.PageDTO;
import com.gmail.derynem.service.model.order.OrderDTO;
import com.gmail.derynem.service.model.user.UserDTO;
import com.gmail.derynem.service.model.user.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static com.gmail.derynem.web.constants.PageNamesConstant.ITEMS_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.ORDERS_PAGE;
import static com.gmail.derynem.web.constants.PageNamesConstant.ORDER_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_LIMIT;
import static com.gmail.derynem.web.constants.PageParamConstant.DEFAULT_PAGE;
import static com.gmail.derynem.web.constants.PageParamConstant.MESSAGE_PARAM;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_NOT_FOUND;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_ORDERS_PAGE;
import static com.gmail.derynem.web.constants.RedirectConstant.REDIRECT_USER_ORDERS;

@Controller
public class OrderController {
    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final ItemService itemService;

    public OrderController(OrderService orderService,
                           ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @GetMapping("/private/orders")
    public String showOrdersPage(Model model,
                                 @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                 @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit) {
        PageDTO<OrderDTO> orderPageInfo = orderService.getOrderPageInfo(page, limit, null);
        model.addAttribute("page", orderPageInfo);
        return ORDERS_PAGE;
    }

    @GetMapping("/private/orders/{id}")
    public String showOrderPage(Model model,
                                @PathVariable(name = "id") Long id) {
        try {
            OrderDTO order = orderService.getById(id);
            model.addAttribute("order", order);
            return ORDER_PAGE;
        } catch (OrderServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/private/order/status")
    public String changeOrderStatus(@ModelAttribute(name = "order") OrderDTO orderDTO) {
        try {
            orderService.changeStatus(orderDTO.getId(), orderDTO.getStatus());
            return REDIRECT_ORDERS_PAGE + String.format(MESSAGE_PARAM, "order status changed");
        } catch (OrderServiceException e) {
            logger.error(e.getMessage(), e);
            return REDIRECT_NOT_FOUND;
        }
    }

    @PostMapping("/user/orders/new")
    public String addOrder(@ModelAttribute(value = "order") @Valid OrderDTO orderDTO,
                           BindingResult bindingResult,
                           Model model,
                           Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserDTO currentUser = userPrincipal.getUser();
        if (bindingResult.hasErrors()) {
            logger.info(" order quantity not valid");
            model.addAttribute("user", currentUser);
            model.addAttribute("page",
                    itemService.getItemPageInfo(Integer.valueOf(DEFAULT_PAGE), Integer.valueOf(DEFAULT_LIMIT)));
            return ITEMS_PAGE;
        }
        orderDTO.getUser().setId(currentUser.getId());
        orderService.save(orderDTO);
        return REDIRECT_USER_ORDERS + String.format(MESSAGE_PARAM, "item ordered!");
    }

    @GetMapping("/user/orders")
    public String showUserOrders(Model model,
                                 @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                 @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
                                 Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        PageDTO<OrderDTO> pageDTO = orderService.getOrderPageInfo(page, limit, userPrincipal.getUser());
        model.addAttribute("page", pageDTO);
        return ORDERS_PAGE;
    }
}
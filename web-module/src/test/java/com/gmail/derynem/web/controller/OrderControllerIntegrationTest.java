package com.gmail.derynem.web.controller;

import com.gmail.derynem.service.model.enums.OrderStatus;
import com.gmail.derynem.service.model.order.OrderDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class OrderControllerIntegrationTest {
    private final String saleEmail = "sale@sale";
    private final String customerEmail = "customer@customer";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.
                webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldShowOrderPage() throws Exception {
        this.mockMvc.perform(get("/private/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldShowSingleOrderPage() throws Exception {
        this.mockMvc.perform(get("/private/orders/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldRedirectAt404IfOrderDoesntExist() throws Exception {
        this.mockMvc.perform(get("/private/orders/9999"))
                .andExpect(redirectedUrl("/404"));
    }

    @Test
    @WithUserDetails(value = saleEmail)
    public void shouldChangeOrderStatus() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setStatus(OrderStatus.REJECTED);
        this.mockMvc.perform(post("/private/order/status")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", orderDTO))
                .andExpect(redirectedUrl("/private/orders?message=order status changed"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldAddOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setQuantity("2");
        orderDTO.getItem().setId(1L);
        orderDTO.getUser().setId(1L);
        this.mockMvc.perform(post("/user/orders/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("order", orderDTO))
                .andExpect(redirectedUrl("/user/orders?message=item ordered!"));
    }

    @Test
    @WithUserDetails(value = customerEmail)
    public void shouldShowUserOrdersPage() throws Exception {
        this.mockMvc.perform(get("/user/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("page"));
    }
}
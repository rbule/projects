package com.pmf.course.order_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmf.course.order_service.classes.Order;
import com.pmf.course.order_service.classes.SentOrder;
import com.pmf.course.order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class OrderServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_shouldReturnAcceptedOrder() throws Exception {
        SentOrder sentOrder = new SentOrder(1L, 2, 4.5f);
        Order order = new Order(99L, 10L, 2, 4.5f);

        when(orderService.newOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/order")
                        .header("X-User-Id", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sentOrder)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.buyerId").value(10))
                .andExpect(jsonPath("$.amountBought").value(2))
                .andExpect(jsonPath("$.rating").value(4.5f));

        verify(orderService, times(1)).newOrder(any(Order.class));
    }

    @Test
    void orderStatus_shouldReturnStatus() throws Exception {
        when(orderService.isOrderRejected(5L)).thenReturn(Map.of("status", "REJECTED"));

        mockMvc.perform(get("/order/status/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        verify(orderService).isOrderRejected(5L);
    }
}


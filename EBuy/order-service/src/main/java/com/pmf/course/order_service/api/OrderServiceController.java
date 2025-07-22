package com.pmf.course.order_service.api;

import com.pmf.course.order_service.classes.Order;
import com.pmf.course.order_service.classes.SentOrder;
import com.pmf.course.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderServiceController {
    private final OrderService orderService;

    public OrderServiceController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Order createOrder(@RequestBody SentOrder request,
                                             @RequestHeader("X-User-Id") Long userId) {
        Order order = new Order(
                request.id(),
                userId,
                request.amountBought(),
                request.rating()
        );

        return orderService.newOrder(order);
    }

    @GetMapping("/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> orderStatus(@PathVariable Long id){
        return orderService.isOrderRejected(id);
    }

    @GetMapping("/myOrders")
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getUsersOrders(@RequestHeader("X-User-Id") Long userId){
        return orderService.getUsersOrders(userId);
    }
}
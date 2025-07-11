package com.pmf.course.order_service.queue;

import com.pmf.course.order_service.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockRejectionListener {

    private final OrderService orderService;

    public StockRejectionListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "q.stock-rejection")
    public void handleStockRejection(StockRejectionEvent event) {
        System.out.println("Order ID received: " + event.orderId() + " " + event.reason());
        orderService.updateStatus(event.orderId(),event.reason());
    }
}
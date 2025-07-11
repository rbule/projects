package com.pmf.course.products_service.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductQueuePublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductQueuePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendStockRejection(StockRejectionEvent event) {
        rabbitTemplate.convertAndSend(
                "x.stock-events",
                "stock.rejected",
                event
        );
    }
}
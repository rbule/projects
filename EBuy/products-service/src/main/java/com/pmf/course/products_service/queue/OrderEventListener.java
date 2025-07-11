package com.pmf.course.products_service.queue;

import com.pmf.course.products_service.service.ProductsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private final ProductsService productService;
    private final ProductQueuePublisher productQueuePublisher;

    public OrderEventListener(ProductsService productService, ProductQueuePublisher productQueuePublisher) {
        this.productService = productService;
        this.productQueuePublisher = productQueuePublisher;
    }

    @RabbitListener(queues = "q.order-events")
    public void handleOrderEvent(OrderEvent event) {
        boolean success = productService.tryReduceStock(event);

        if (!success) {
            productQueuePublisher.sendStockRejection(new StockRejectionEvent(event.id(),"REJECTED"));
        }
        else{
            productService.updateRating(event.productId(),event.rating());
            productQueuePublisher.sendStockRejection(new StockRejectionEvent(event.id(), "CREATED"));
        }
    }
}

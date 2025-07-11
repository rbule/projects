package com.pmf.course.order_service.queue;

import com.pmf.course.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import static com.pmf.course.order_service.queue.RabbitMQConfig.ORDER_EXCHANGE_NAME;
import static com.pmf.course.order_service.queue.RabbitMQConfig.ORDER_QUEUE_NAME;

@Service
public class OrderQueue {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderQueue(OrderRepository orderRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderEvent(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getItemId(),
                order.getBuyerId(),
                order.getAmountBought(),
                order.getRating()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE_NAME, RabbitMQConfig.ORDER_QUEUE_NAME, event);
    }
}

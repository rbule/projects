package com.pmf.course.order_service.service;

import com.pmf.course.order_service.classes.Order;
import com.pmf.course.order_service.classes.OrderEntity;
import com.pmf.course.order_service.exceptions.OrderNotFoundException;
import com.pmf.course.order_service.queue.OrderEvent;
import com.pmf.course.order_service.queue.OrderQueue;
import com.pmf.course.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OrderQueue orderQueue;

    public OrderService(OrderRepository orderRepository,OrderQueue orderQueue) {
        this.orderRepository = orderRepository;
        this.orderQueue = orderQueue;
    }

    public Order newOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity(order);

        OrderEntity saved = orderRepository.save(orderEntity);

        OrderEvent event = new OrderEvent(
                saved.getId(),saved.getItemId(), saved.getBuyerId(), saved.getAmountBought(), saved.getRating()
        );

        orderQueue.publishOrderEvent(event.id());

        return new Order(saved);
    }

    public Map<String,String> isOrderRejected(Long id){
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException());

        return Map.of("status",order.getStatus());
    }

    public void updateStatus(Long orderId, String status) {
        int retries = 5;
        int delayMs = 200;

        for (int i = 0; i < retries; i++) {
            Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                OrderEntity order = optionalOrder.get();
                order.setStatus(status);
                orderRepository.save(order);
                return;
            }
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException ignored) {}
        }

        throw new OrderNotFoundException();
    }

    public List<Order> getUsersOrders(Long userId) {
        List<OrderEntity> orderEntities = orderRepository.findAllByBuyerId(userId);
        return orderEntities.stream()
                .map(Order::new)
                .collect(Collectors.toList());
    }

}

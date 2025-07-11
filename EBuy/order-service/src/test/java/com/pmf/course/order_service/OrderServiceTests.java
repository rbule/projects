package com.pmf.course.order_service;

import com.pmf.course.order_service.classes.Order;
import com.pmf.course.order_service.classes.OrderEntity;
import com.pmf.course.order_service.queue.OrderQueue;
import com.pmf.course.order_service.repository.OrderRepository;
import com.pmf.course.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderQueue orderQueue;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNewOrder_savesOrderAndPublishesEvent() {
        Order inputOrder = new Order(10L, 20L, 2, 4.5f);
        OrderEntity savedEntity = new OrderEntity(inputOrder);

        // manually set the ID as if the DB did it
        ReflectionTestUtils.setField(savedEntity, "id", 99L);

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedEntity);

        Order result = orderService.newOrder(inputOrder);

        assertNotNull(result);
        assertEquals(99L, result.id());

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(orderQueue, times(1)).publishOrderEvent(99L);
    }

    @Test
    void testIsOrderRejected_returnsStatus() {
        OrderEntity orderEntity = new OrderEntity(new Order(1L, 2L, 1, 4.5f));
        orderEntity.setStatus("REJECTED");
        ReflectionTestUtils.setField(orderEntity, "id", 5L);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(orderEntity));

        Map<String, String> status = orderService.isOrderRejected(5L);

        assertEquals("REJECTED", status.get("status"));
    }

    @Test
    void testUpdateStatus_updatesOrderWhenFound() {
        OrderEntity orderEntity = new OrderEntity(new Order(1L, 2L, 1, 4.5f));
        orderEntity.setStatus("PENDING");
        ReflectionTestUtils.setField(orderEntity, "id", 10L);

        when(orderRepository.findById(10L)).thenReturn(Optional.of(orderEntity));

        orderService.updateStatus(10L, "CONFIRMED");

        assertEquals("CONFIRMED", orderEntity.getStatus());
        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    void testUpdateStatus_throwsWhenNotFound() {
        when(orderRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.updateStatus(123L, "ANYTHING"));

        assertTrue(exception.getMessage().contains("Order not found"));
    }
}

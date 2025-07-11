package com.pmf.course.order_service.repository;

import com.pmf.course.order_service.classes.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}

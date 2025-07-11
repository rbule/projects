package com.pmf.course.order_service.queue;

public record StockRejectionEvent(Long orderId,String reason) {
}

package com.pmf.course.products_service.queue;

public record StockRejectionEvent(
        Long orderId,
        String reason
) {}
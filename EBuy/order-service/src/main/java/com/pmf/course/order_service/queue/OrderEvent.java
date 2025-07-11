package com.pmf.course.order_service.queue;

public record OrderEvent(
        Long id,
        Long productId,
        Long buyerId,
        Integer amountBought,
        Float rating
) {
}

package com.pmf.course.products_service.queue;

public record OrderEvent(
        Long id,
        Long productId,
        Long buyerId,
        Integer amountBought,
        Float rating
) {}

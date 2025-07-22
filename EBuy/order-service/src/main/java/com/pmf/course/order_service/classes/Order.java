package com.pmf.course.order_service.classes;

public record Order(Long id,
    Long itemId,
    Long buyerId,
    Integer amountBought,
    Float rating
) {
    public Order(OrderEntity orderEntity){
        this(orderEntity.getId(), orderEntity.getItemId(), orderEntity.getBuyerId(),
        orderEntity.getAmountBought(),
        orderEntity.getRating());
    }
    public Order(Long itemId, Long buyerId, Integer amountBought, Float rating) {
        this(0L, itemId, buyerId, amountBought, rating);
    }
}

package com.pmf.course.order_service.classes;

public record Order(Long id,
    Long buyerId,
    Integer amountBought,
    Float rating
) {
    public Order(OrderEntity orderEntity){
        this(orderEntity.getId(), orderEntity.getBuyerId(),
        orderEntity.getAmountBought(),
        orderEntity.getRating());
    }
}

package com.pmf.course.order_service.classes;

public record SentOrder(Long id,
                    Integer amountBought,
                    Float rating
) {
    public SentOrder(OrderEntity orderEntity){
        this(orderEntity.getId(),
                orderEntity.getAmountBought(),
                orderEntity.getRating());
    }
}

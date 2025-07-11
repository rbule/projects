package com.pmf.course.products_service.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record Product(
        Long id,
        Long sellerId,
        @NotBlank String name,
        @PositiveOrZero Float price,
        String description,
        @PositiveOrZero Integer quantity,
        Float rating,
        Integer numberOfRatings,
        Float totalSum) {
    public Product(ProductEntity entity){
        this(entity.getId(), entity.getSellerId(), entity.getName(), entity.getPrice(), entity.getDescription(), entity.getQuantity(),entity.getRating(),entity.getNumberOfRatings(),entity.getTotalSum());
    }
}

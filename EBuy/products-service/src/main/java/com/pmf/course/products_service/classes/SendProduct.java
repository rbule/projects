package com.pmf.course.products_service.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record SendProduct(
        Long id,
        Long sellerId,
        @NotBlank String name,
        @PositiveOrZero Float price,
        String description,
        @PositiveOrZero Integer quantity,
        Float rating) {
    public SendProduct(ProductEntity entity){
        this(entity.getId(), entity.getSellerId(), entity.getName(), entity.getPrice(), entity.getDescription(), entity.getQuantity(),entity.getRating());
    }
    public SendProduct(Product product){
        this(product.id(), product.sellerId(), product.name(), product.price(), product.description(), product.quantity(), product.rating());
    }
}
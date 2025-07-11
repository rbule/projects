package com.pmf.course.products_service.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record NewProduct(@NotBlank String name,
                         @PositiveOrZero Float price,
                         String description,
                         @PositiveOrZero Integer quantity) {
}

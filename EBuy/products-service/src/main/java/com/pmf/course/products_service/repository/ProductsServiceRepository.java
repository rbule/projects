package com.pmf.course.products_service.repository;

import com.pmf.course.products_service.classes.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsServiceRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findByNameContainingIgnoreCase(String namePart);
}

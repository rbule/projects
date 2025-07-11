package com.pmf.course.products_service;

import com.pmf.course.products_service.classes.ProductEntity;
import com.pmf.course.products_service.classes.*;
import com.pmf.course.products_service.queue.OrderEvent;
import com.pmf.course.products_service.queue.ProductQueuePublisher;
import com.pmf.course.products_service.repository.ProductsServiceRepository;
import com.pmf.course.products_service.service.ProductsService;
import com.pmf.course.products_service.exceptions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductsServiceTest {

    private ProductsServiceRepository repository;
    private ProductsService service;
    private ProductQueuePublisher queuePublisher;

    @BeforeEach
    void setUp() {
        repository = mock(ProductsServiceRepository.class);
        service = new ProductsService(repository,queuePublisher);
    }

    @Test
    void getAllProducts_shouldReturnMappedList() {
        List<ProductEntity> entities = List.of(
                new ProductEntity(1L, "Phone", 500f, "Nice phone", 10, 0f, 0, 0f),
                new ProductEntity(1L, "Laptop", 1200f, "Fast laptop", 5, 0f, 0, 0f)
        );

        when(repository.findAll()).thenReturn(entities);

        List<SendProduct> result = service.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Phone", result.get(0).name());
        assertEquals("Laptop", result.get(1).name());
    }

    @Test
    void getProductById_shouldReturnMappedProduct() {
        ProductEntity entity = new ProductEntity(1L, "Monitor", 250f, "4K monitor", 20, 0f, 0, 0f);

        when(repository.findById(5L)).thenReturn(Optional.of(entity));

        SendProduct result = service.getProductById(5L);

        assertEquals("Monitor", result.name());
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.getProductById(100L));
    }

    @Test
    void getProductsByName_shouldReturnMatchingList() {
        ProductEntity entity = new ProductEntity(1L, "Mouse", 50f, "Gaming mouse", 15, 0f, 0, 0f);

        when(repository.findByNameContainingIgnoreCase("mouse")).thenReturn(List.of(entity));

        List<SendProduct> result = service.getProductsByName("mouse");

        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).name());
    }

    @Test
    void getProductsByName_shouldThrow_whenEmpty() {
        assertThrows(IncorrectSearchInputException.class, () -> service.getProductsByName(""));
    }

    @Test
    void tryReduceStock_shouldReturnTrue_whenEnoughStock() {
        ProductEntity product = new ProductEntity(1L, "Speaker", 100f, "Bluetooth", 10, 0f, 0, 0f);
        OrderEvent order = new OrderEvent(1L, product.getId(), 2L, 5, 0f);

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        boolean result = service.tryReduceStock(order);

        assertTrue(result);
        assertEquals(5, product.getQuantity());
        verify(repository).save(product);
    }

    @Test
    void tryReduceStock_shouldReturnFalse_whenNotEnoughStock() {
        ProductEntity product = new ProductEntity(1L, "Tablet", 300f, "Android", 3, 0f, 0, 0f);
        OrderEvent order = new OrderEvent(1L, product.getId(), 2L, 5, 0f);

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        boolean result = service.tryReduceStock(order);

        assertFalse(result);
        verify(repository, never()).save(any());
    }

    @Test
    void tryReduceStock_shouldReturnFalse_whenProductMissing() {
        OrderEvent order = new OrderEvent(1L, 500L, 2L, 2, 0f);
        when(repository.findById(500L)).thenReturn(Optional.empty());

        boolean result = service.tryReduceStock(order);

        assertFalse(result);
    }

    @Test
    void newProduct_shouldSaveProductAndReturnSendProduct() {
        NewProduct input = new NewProduct("Watch", 200f, "Smartwatch", 30);
        ProductEntity saved = new ProductEntity(10L, "Watch", 200f, "Smartwatch", 30, 0f, 0, 0f);

        when(repository.save(any())).thenReturn(saved);

        SendProduct result = service.newProduct(input, 10L);

        assertEquals("Watch", result.name());
    }

    @Test
    void updateRating_shouldCalculateAndSaveCorrectly() {
        ProductEntity entity = new ProductEntity(10L, "Camera", 800f, "DSLR", 5, 0f, 2, 8.0f);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.updateRating(1L, 4.0f);

        verify(repository).save(argThat(p ->
                p.getRating() == 4.0f &&
                        p.getNumberOfRatings() == 3 &&
                        p.getTotalSum() == 12.0f
        ));
    }

    @Test
    void updateRating_shouldThrow_whenProductNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.updateRating(1L, 5.0f));
    }
}


package com.pmf.course.products_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmf.course.products_service.classes.NewProduct;
import com.pmf.course.products_service.classes.SendProduct;
import com.pmf.course.products_service.service.ProductsService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ProductsServiceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductsService productsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getProducts_shouldReturnListOfProducts() throws Exception {
        SendProduct p1 = new SendProduct(1L, 2L, "Phone", 500f, "Smart", 10, 4.5f);
        SendProduct p2 = new SendProduct(2L, 2L, "Laptop", 1000f, "Gaming", 5, 4.7f);

        when(productsService.getAllProducts()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Phone"))
                .andExpect(jsonPath("$[1].name").value("Laptop"));
    }

    @Test
    void getProduct_shouldReturnProductById() throws Exception {
        SendProduct product = new SendProduct(5L, 1L, "Monitor", 250f, "4K", 12, 4.2f);

        when(productsService.getProductById(5L)).thenReturn(product);

        mockMvc.perform(get("/products/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Monitor"))
                .andExpect(jsonPath("$.price").value(250.0));
    }

    @Test
    void getProducts_shouldReturnFilteredProductsByQuery() throws Exception {
        SendProduct match = new SendProduct(3L, 1L, "Keyboard", 80f, "Mechanical", 8, 4.0f);

        when(productsService.getProductsByName("key")).thenReturn(List.of(match));

        mockMvc.perform(get("/products/search").param("query", "key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Keyboard"));
    }

    @Test
    void newProduct_shouldCreateProduct() throws Exception {
        NewProduct request = new NewProduct("Tablet", 299.99f, "Android tablet", 20);
        SendProduct response = new SendProduct(10L, 7L, "Tablet", 299.99f, "Android tablet", 20, 0f);

        when(productsService.newProduct(any(NewProduct.class), eq(7L))).thenReturn(response);

        mockMvc.perform(post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Id", "7"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.price").value(299.99));
    }
}

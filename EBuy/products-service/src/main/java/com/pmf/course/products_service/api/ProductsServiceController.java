package com.pmf.course.products_service.api;

import com.pmf.course.products_service.classes.NewProduct;
import com.pmf.course.products_service.classes.SendProduct;
import com.pmf.course.products_service.service.ProductsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsServiceController {
    private final ProductsService productsService;

    public ProductsServiceController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SendProduct> getProducts() {
        return productsService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SendProduct getProduct(@PathVariable Long id) {
        return productsService.getProductById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<SendProduct> getProducts(@RequestParam String query) {
        return productsService.getProductsByName(query);
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public SendProduct newProduct(@RequestBody NewProduct newProduct,@RequestHeader("X-User-Id") Long userId) {
        return productsService.newProduct(newProduct,userId);
    }
}

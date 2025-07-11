package com.pmf.course.products_service.service;

import com.pmf.course.products_service.classes.NewProduct;
import com.pmf.course.products_service.classes.ProductEntity;
import com.pmf.course.products_service.classes.SendProduct;
import com.pmf.course.products_service.exceptions.IncorrectSearchInputException;
import com.pmf.course.products_service.exceptions.ProductNotFoundException;
import com.pmf.course.products_service.queue.OrderEvent;
import com.pmf.course.products_service.queue.ProductQueuePublisher;
import com.pmf.course.products_service.repository.ProductsServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsService {
    private final ProductsServiceRepository productsServiceRepository;
    private final ProductQueuePublisher productQueuePublisher;

    public ProductsService(ProductsServiceRepository productsServiceRepository, ProductQueuePublisher productQueuePublisher) {
        this.productsServiceRepository = productsServiceRepository;
        this.productQueuePublisher = productQueuePublisher;
    }

    public List<SendProduct> getAllProducts(){
        return productsServiceRepository.findAll().stream().map(SendProduct::new).collect(Collectors.toList());
    }

    public SendProduct getProductById(Long id){
        return productsServiceRepository.findById(id).map(SendProduct::new).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<SendProduct> getProductsByName(String name){
        if(name.isEmpty()){
            throw new IncorrectSearchInputException();
        }
        return productsServiceRepository.findByNameContainingIgnoreCase(name).stream().map(SendProduct::new).collect(Collectors.toList());
    }

    public boolean tryReduceStock(OrderEvent order) {
        var productOpt = productsServiceRepository.findById(order.productId());

        if (productOpt.isEmpty()) return false;
        var product = productOpt.get();
        if (product.getQuantity() < order.amountBought()) {
            return false;
        }

        product.setQuantity(product.getQuantity() - order.amountBought());
        productsServiceRepository.save(product);

        return true;
    }


    public SendProduct newProduct(NewProduct newProduct, Long userId) {
        ProductEntity entity = new ProductEntity(userId,newProduct.name(),newProduct.price(),newProduct.description(),newProduct.quantity(),0.0f,0,0.0f);

        ProductEntity saved = productsServiceRepository.save(entity);
        return new SendProduct(saved);
    }

    public void updateRating(Long id, Float newRating){
        ProductEntity productEntity = productsServiceRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productEntity.setNumberOfRatings(productEntity.getNumberOfRatings()+1);
        productEntity.setTotalSum(productEntity.getTotalSum()+newRating);
        productEntity.setRating(productEntity.getTotalSum()/productEntity.getNumberOfRatings());
        productsServiceRepository.save(productEntity);
    }
}

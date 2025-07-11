package com.pmf.course.products_service.classes;

import jakarta.persistence.*;

@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    Long id;

    @Column
    Long sellerId;

    @Column
    String name;

    @Column
    Float price;

    @Column
    String description;

    @Column
    Integer quantity;

    @Column
    Float rating;

    @Column
    private Integer numberOfRatings;

    @Column
    private Float totalSum;

    public ProductEntity() {}

    public ProductEntity(Long sellerId, String name, Float price, String description, Integer quantity, Float rating,Integer numberOfRatings, Float totalSum) {
        this.sellerId = sellerId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
        this.totalSum = totalSum;
    }

    public ProductEntity(Product product) {
        this.id = product.id();
        this.sellerId = product.sellerId();
        this.name = product.name();
        this.price = product.price();
        this.description = product.description();
        this.quantity = product.quantity();
        this.rating = product.rating();
        this.numberOfRatings = product.numberOfRatings();
        this.totalSum = product.totalSum();
    }

    public String getDescription() {
        return description;
    }

    public Long getSellerId(){
        return sellerId;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

    public Float getRating() {return rating;}

    public Integer getNumberOfRatings() {
        return numberOfRatings;
    }

    public Float getTotalSum() {
        return totalSum;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTotalSum(Float totalSum) {
        this.totalSum = totalSum;
    }

    public void setNumberOfRatings(Integer numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}

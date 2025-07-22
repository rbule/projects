package com.pmf.course.order_service.classes;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "unique_id")
    private Long id;

    @Column(nullable = false,name = "item_id")
    private Long itemId;

    @Column(nullable = false, name = "buyer_id")
    private Long buyerId;

    @Column(nullable = false,name = "amount_bought")
    private Integer amountBought;

    @Column(nullable = false,name = "rating")
    private Float rating;

    @Column(nullable = false,name = "status")
    private String status;

    public OrderEntity() {
    }

    public OrderEntity(Order order) {
        this.itemId = order.itemId();
        this.buyerId = order.buyerId();
        this.amountBought = order.amountBought();
        this.rating = order.rating();
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Integer getAmountBought() {
        return amountBought;
    }

    public Float getRating() {
        return rating;
    }

    public String getStatus() {
        return status;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "buyerId='" + buyerId + '\'' +
                ", amountBought='" + amountBought + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(buyerId, that.buyerId) &&
                Objects.equals(amountBought, that.amountBought) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, buyerId, amountBought, rating);
    }
}
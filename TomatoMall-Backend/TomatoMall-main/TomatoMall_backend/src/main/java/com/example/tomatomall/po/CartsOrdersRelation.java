package com.example.tomatomall.po;

import javax.persistence.*;

@Entity
@Table(name = "carts_orders_relation")
public class CartsOrdersRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer cartitemId;
    private Integer orderId;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCartitemId() {
        return cartitemId;
    }

    public void setCartitemId(Integer cartitemId) {
        this.cartitemId = cartitemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
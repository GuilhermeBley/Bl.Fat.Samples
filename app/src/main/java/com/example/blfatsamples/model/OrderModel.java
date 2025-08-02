package com.example.blfatsamples.model;

import java.util.List;
import java.util.Date;

public class OrderModel {
    private double totalValue;
    private int totalProductQuantity;
    private Date deliveredAt;
    private Date startedDeliveringAt;
    private Date createdAt;
    private List<ProductModel> products;

    public OrderModel(double totalValue, int totalProductQuantity, Date deliveredAt, Date startedDeliveringAt, Date createdAt, List<ProductModel> products) {
        this.totalValue = totalValue;
        this.totalProductQuantity = totalProductQuantity;
        this.deliveredAt = deliveredAt;
        this.startedDeliveringAt = startedDeliveringAt;
        this.createdAt = createdAt;
        this.products = products;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public int getTotalProductQuantity() {
        return totalProductQuantity;
    }

    public void setTotalProductQuantity(int totalProductQuantity) {
        this.totalProductQuantity = totalProductQuantity;
    }

    public Date getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Date getStartedDeliveringAt() {
        return startedDeliveringAt;
    }

    public void setStartedDeliveringAt(Date startedDeliveringAt) {
        this.startedDeliveringAt = startedDeliveringAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}

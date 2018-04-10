package com.example.tienthanh.foodstore;

import java.io.Serializable;

public class OrderDetail implements Serializable{
    private long id;
    private long orderID;
    private long foodID;
    private int amount;
    private Double cost;
    private String foodName;

    public OrderDetail(long id, long orderID, long foodID, int amount, Double cost, String foodName) {
        this.id = id;
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
        this.foodName = foodName;
    }

    public OrderDetail(long orderID, long foodID, int amount, Double cost, String foodName) {
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
        this.foodName = foodName;
    }

    public OrderDetail(long orderID, long foodID, int amount, Double cost) {
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getFoodID() {
        return foodID;
    }

    public void setFoodID(long foodID) {
        this.foodID = foodID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public OrderDetail() {

    }
}

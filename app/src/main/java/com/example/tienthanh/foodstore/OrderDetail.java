package com.example.tienthanh.foodstore;

public class OrderDetail {
    private long id;
    private int orderID;
    private int foodID;
    private int amount;
    private Double cost;
    private String foodName;

    public OrderDetail(long id, int orderID, int foodID, int amount, Double cost, String foodName) {
        this.id = id;
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
        this.foodName = foodName;
    }

    public OrderDetail(long id, int orderID, int foodID, int amount, Double cost) {
        this.id = id;
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
    }

    public OrderDetail(int orderID, int foodID, int amount, Double cost) {
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

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
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
}

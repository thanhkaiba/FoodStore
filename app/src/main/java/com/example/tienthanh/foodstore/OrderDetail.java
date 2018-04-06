package com.example.tienthanh.foodstore;

public class OrderDetail {
    private int id;
    private int orderID;
    private int foodID;
    private int amount;
    private int cost;

    public OrderDetail(int id, int orderID, int foodID, int amount, int cost) {
        this.id = id;
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
    }

    public OrderDetail(int orderID, int foodID, int amount, int cost) {
        this.orderID = orderID;
        this.foodID = foodID;
        this.amount = amount;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}

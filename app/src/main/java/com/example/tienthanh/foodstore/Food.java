package com.example.tienthanh.foodstore;

import java.io.Serializable;

class Food implements Serializable{
    private long id;
    private String name;
    private String type;
    private String description;
    private String img;
    private double cost;
    private String unit;
    private long vendorID;
    private String vendorName;
    private int amount;
    public static final String[] TYPE = {"Vegetable", "Meat", "Fruit", "SeaFood", "Nuts, Grains and Beans"};

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Food(long id, String name, String type, String description, String img, double cost, String unit, long vendorID, String vendorName, int amount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.amount = amount;
    }

    public Food() {

    }


    public Food(String name, String type, String description, String img, double cost, String unit, long vendorID, int amount) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
        this.vendorID = vendorID;
        this.vendorName = vendorName;
        this.amount = amount;
    }

    public long getVendorID() {
        return vendorID;
    }

    public void setVendorID(long vendorID) {
        this.vendorID = vendorID;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

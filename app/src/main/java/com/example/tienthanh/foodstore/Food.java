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
    private int vendorID;
    private String vendorName;

    public Food() {

    }

    public Food(long id, String name, String type, String description, String img, double cost, String unit, int vendor) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
        this.vendorID = vendor;
    }

    public Food(String name, String type, String description, String img, double cost, String unit, int vendor) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
        this.vendorID = vendor;
    }

    public Food(long id, String name, String type, String description, String img, double cost, String unit, int vendorID, String vendorName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
        this.vendorID = vendorID;
        this.vendorName = vendorName;
    }

    public int getVendorID() {
        return vendorID;
    }

    public void setVendorID(int vendorID) {
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

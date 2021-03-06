package com.example.tienthanh.foodstore;

import java.io.Serializable;

class Food implements Serializable{
    private int id;
    private String name;
    private String type;
    private String description;
    private int img;
    private double cost;
    private String unit;

    public Food(int id, String name, String type, String description, int img, double cost, String unit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.img = img;
        this.cost = cost;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
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

    public Food(int id, String name, int img, double cost) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.cost = cost;
    }
}

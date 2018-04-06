package com.example.tienthanh.foodstore;

public class Order {
    private long id;
    private double total;
    private String date;
    private String name;
    private String email;
    private String phone;
    private String address;
    private int amount;
    private int status;

    public Order(long id, double total, String date, String name, String email, String phone, String address, int amount, int status) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.amount = amount;
        this.status = status;
    }

    public Order(double total, String date, String name, String email, String phone, String address, int amount, int status) {
        this.total = total;
        this.date = date;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.amount = amount;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

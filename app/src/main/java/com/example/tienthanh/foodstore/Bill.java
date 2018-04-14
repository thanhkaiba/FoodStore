package com.example.tienthanh.foodstore;

public class Bill {
    private long id;
    private double total;
    private String date;
    private int type;
    private int amount;
    private long userID;
    private String userName;

    public static final int SELL = 0;
    public static final int ENTER = 1;
    public static final int RETURN = 2;

    public Bill(long id, double total, String date, int type, int amount, long userID, String userName) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.userID = userID;
        this.userName = userName;
    }

    public Bill(long id, double total, String date, int type, int amount, long userID) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.userID = userID;
    }

    public Bill(double total, String date, int type, int amount, long userID) {
        this.total = total;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.userID = userID;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

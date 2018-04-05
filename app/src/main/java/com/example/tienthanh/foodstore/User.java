package com.example.tienthanh.foodstore;

import java.io.Serializable;


public class User implements Serializable {
    public static final String[] GENDER =  {"Nam", "Nữ"};
    private long id;
    private String password;
    private String img;
    private String name;
    private String gender;
    private String birthday;
    private String email;
    private String phone;
    private int privilege;
    private String address;

    public static final String[] PRIVILEGE = {"Manager", "Salesman / Saleswoman", "Storekeeper", "Customer" };

    public User(long id, String password, String img, String name,
                String gender, String birthday, String email, String phone, int privilege, String address) {
        this.id = id;
        this.password = password;
        this.img = img;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.privilege = privilege;
        this.address = address;
    }

    public User(String password, String img, String name,
                String gender, String birthday, String email, String phone, int privilege, String address) {
        this.password = password;
        this.img = img;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.privilege = privilege;
        this.address = address;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

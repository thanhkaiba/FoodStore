package com.example.tienthanh.foodstore;

public class User {
    private int id;
    private String user;
    private String password;
    private String img;
    private String name;
    private String gender;
    private String birthday;
    private String email;
    private  String phone;
    private int privilege;

    public User(int id, String user, String password, String img, String name, String gender, String birthday, String email, String phone, int privilege) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.img = img;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.privilege = privilege;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public User(String user, String password, String img, String name, String gender, String birthday, String email, String phone, int privilege) {
        this.user = user;
        this.password = password;
        this.img = img;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.privilege = privilege;
    }
}

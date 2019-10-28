package com.example.orderedodriver.model;

import java.io.Serializable;

public class User implements Serializable {
    double rate;
    String username ;
    String email ;
    String image ;
    String phone ;
    String type ;

    public User() {
    }

    public User(String username, String email, String image, String phone, String type) {
        this.username = username;
        this.email = email;
        this.image = image;
        this.phone = phone;
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

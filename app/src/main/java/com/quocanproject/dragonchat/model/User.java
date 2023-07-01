package com.quocanproject.dragonchat.model;

import com.google.firebase.Timestamp;

public class User {

    private String phone;
    private String username;
    private Timestamp timeCreated;

    public User() {
    }

    public User(String phone, String username, Timestamp timeCreated) {
        this.phone = phone;
        this.username = username;
        this.timeCreated = timeCreated;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }
}

package com.quocanproject.dragonchat.model;

import com.google.firebase.Timestamp;

public class User {

    private String phone;
    private String username;
    private Timestamp timeCreated;

    private String uID;

    private String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public User() {
    }

    public User(String phone, String username, Timestamp timeCreated, String id) {
        this.phone = phone;
        this.username = username;
        this.timeCreated = timeCreated;
        this.uID = id;
    }

    public String getPhone() {
        return phone;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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

package com.quocanproject.dragonchat.model;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private String msg;
    private String senderID;
    private Timestamp timestamp;



    public ChatMessage() {
    }

    public ChatMessage(String msg, String senderID, Timestamp timestamp) {
        this.msg = msg;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

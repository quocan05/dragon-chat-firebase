package com.quocanproject.dragonchat.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoom {
    private String chatRoomId;
    private List<String> userIDs;
    private Timestamp lastMsgTimestamp;
    private String lastMsgSenderID;

    public ChatRoom() {
    }

    public ChatRoom(String chatRoomId, List<String> userIDs, Timestamp lastMsgTimestamp, String lastMsgSenderID) {
        this.chatRoomId = chatRoomId;
        this.userIDs = userIDs;
        this.lastMsgTimestamp = lastMsgTimestamp;
        this.lastMsgSenderID = lastMsgSenderID;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public Timestamp getLastMsgTimestamp() {
        return lastMsgTimestamp;
    }

    public void setLastMsgTimestamp(Timestamp lastMsgTimestamp) {
        this.lastMsgTimestamp = lastMsgTimestamp;
    }

    public String getLastMsgSenderID() {
        return lastMsgSenderID;
    }

    public void setLastMsgSenderID(String lastMsgSenderID) {
        this.lastMsgSenderID = lastMsgSenderID;
    }
}

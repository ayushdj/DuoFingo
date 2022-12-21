package com.example.duofingo;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private final boolean isMessageTypeIncoming;

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.isMessageTypeIncoming = true;
        // Initialize to current time
        messageTime = new Date().getTime();
    }
    public ChatMessage(String messageText, String messageUser, long timestamp) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = timestamp;
        this.isMessageTypeIncoming = true;
    }

    public boolean isMessageTypeIncoming() {
        return isMessageTypeIncoming;
    }

    public ChatMessage(String messageText, String messageUser, long timestamp, boolean flag){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = timestamp;
        this.isMessageTypeIncoming = flag;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
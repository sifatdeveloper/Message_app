package com.example.mymessage;

public class Message {
    private int id;
    private String phoneNumber;
    private String messageText;

    public Message(int id, String phoneNumber, String messageText) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.messageText = messageText;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageText() {
        return messageText;
    }
}

package com.projetomobile.jpm.healthunits.valueobject;

import java.util.Date;


public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageBitmap;

    public ChatMessage(String messageText, String messageUser, String messageBitmap) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
        this.messageBitmap = messageBitmap;
    }

    public ChatMessage() {
    }

    public String getMessageBitmap() {
        return messageBitmap;
    }

    public void setMessageBitmap(String messageBitmap) {
        this.messageBitmap = messageBitmap;
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

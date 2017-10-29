package com.projetomobile.jpm.healthunits.valueobject;

import android.graphics.Bitmap;

import java.util.Date;


public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private Bitmap bm;

    public ChatMessage(String messageText, String messageUser, Bitmap bm) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
        this.bm = bm;
    }

    public ChatMessage() {
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

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }
}

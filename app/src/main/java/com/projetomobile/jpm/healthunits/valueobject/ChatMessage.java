package com.projetomobile.jpm.healthunits.valueobject;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;


public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageBitmap;
    private String temImagem;
    private String latitude;
    private String longitude;

    public ChatMessage(String messageText, String messageUser, String messageBitmap, String temImagem, LatLng latLng) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
        this.messageBitmap = messageBitmap;
        this.temImagem = temImagem;
        this.latitude = String.valueOf(latLng.latitude);
        this.longitude = String.valueOf(latLng.longitude);
    }

    public ChatMessage() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTemImagem() {
        return temImagem;
    }

    public void setTemImagem(String temImagem) {
        this.temImagem = temImagem;
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

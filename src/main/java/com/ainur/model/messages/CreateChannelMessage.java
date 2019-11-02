package com.ainur.model.messages;

public class CreateChannelMessage {
    private String token;
    private String chanelName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChanelName() {
        return chanelName;
    }

    public void setChanelName(String chanelName) {
        this.chanelName = chanelName;
    }


}

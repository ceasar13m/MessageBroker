package com.ainur.model.messages;

public class CreateChannelMessage {
    private String token;
    private String channelName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String chanelName) {
        this.channelName = chanelName;
    }


}

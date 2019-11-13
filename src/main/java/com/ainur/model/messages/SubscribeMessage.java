package com.ainur.model.messages;


import org.springframework.stereotype.Component;

public class SubscribeMessage {
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

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

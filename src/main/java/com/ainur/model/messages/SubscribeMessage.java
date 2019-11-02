package com.ainur.model.messages;

public class SubscribeMessage {
    private String token;
    private String chanel;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getChanel() {
        return chanel;
    }

    public void setChanel(String chanel) {
        this.chanel = chanel;
    }
}

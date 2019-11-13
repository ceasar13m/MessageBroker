package com.ainur.model.messages;


import org.springframework.stereotype.Component;

public class DisconnectMessage {

    private String token;

    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

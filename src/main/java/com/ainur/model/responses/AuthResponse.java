package com.ainur.model.responses;

import org.springframework.stereotype.Component;

@Component
public class AuthResponse {
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

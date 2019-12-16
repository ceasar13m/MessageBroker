package com.ainur.model.responses;

import org.springframework.stereotype.Component;

@Component
public class StatusResponse {

    private int statusCode;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getToken() {
        return token;
    }
}

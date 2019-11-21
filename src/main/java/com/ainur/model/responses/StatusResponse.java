package com.ainur.model.responses;

import org.springframework.stereotype.Component;

@Component("statusResponse")
public class StatusResponse {

    private int statusCode;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


}

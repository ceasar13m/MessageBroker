package com.ainur.model.messages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PublishMessage {
    private String token;
    private String channelName;
    private String message;
    private String sendDateString;
    private Date sendDate = new Date();
    SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public PublishMessage() {
        sendDateString = formatForDateNow.format(sendDate);
    }

    public String getDateString() {
        return sendDateString;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

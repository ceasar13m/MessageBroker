package com.ainur.model.messages;

public class MessagePocket {
    private String Sender;
    private String channelName;
    private String message;
    private String sendDateString;

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
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

    public String getSendDateString() {
        return sendDateString;
    }

    public void setSendDateString(String sendDateString) {
        this.sendDateString = sendDateString;
    }
}

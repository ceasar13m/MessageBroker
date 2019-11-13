package com.ainur.model.messages;


import org.springframework.stereotype.Component;

public class Message {
    private int command;
    private String data;


    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

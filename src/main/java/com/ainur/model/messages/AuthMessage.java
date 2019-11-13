package com.ainur.model.messages;

import org.springframework.stereotype.Component;

import java.net.Socket;


public class AuthMessage {
    private Socket socket;
    private Message message;

    public AuthMessage(Message message, Socket socket) {
        this.socket = socket;
        this.message = message;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

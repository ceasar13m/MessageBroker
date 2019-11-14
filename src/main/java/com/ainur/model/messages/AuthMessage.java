package com.ainur.model.messages;

import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;

import java.net.Socket;


public class AuthMessage {
    private WebSocket socket;
    private Message message;


    public AuthMessage(Message message, WebSocket socket) {
        this.socket = socket;
        this.message = message;
    }

    public WebSocket getSocket() {
        return socket;
    }

    public void setSocket(WebSocket socket) {
        this.socket = socket;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}

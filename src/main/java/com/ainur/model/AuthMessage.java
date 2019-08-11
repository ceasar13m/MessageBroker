package com.ainur.model;

import java.net.Socket;

public class AuthMessage {
    private Socket socket;
    private Message message;

    public AuthMessage(Message message, Socket socket) {
        this.socket = socket;
        this.message = message;
    }
}

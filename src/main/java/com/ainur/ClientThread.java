package com.ainur;

import com.ainur.model.messages.Message;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;

public class ClientThread {

    private WebSocket socket;
    private MessageProcessor processor;
    private Gson gson = new Gson();
    String jsonMessage;


    public ClientThread(MessageProcessor processor, WebSocket socket, String message) {
        this.socket = socket;
        this.processor = processor;
        jsonMessage = message;
    }

    public void run() {
        Message message = gson.fromJson(jsonMessage, Message.class);
        processor.addMessage(message, socket);
    }
}

package com.ainur;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer {

    private MessageProcessor processor;
    public Server(InetSocketAddress address) {
        super(address);
        processor = new MessageProcessor(WebSocketsStorage.getWebSocketsStorage());
        processor.startWorkers();
        TokensStorage.getTokenStorage();
    }


    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        conn.close();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        ClientThread clientThread = new ClientThread(processor, conn, message);
        clientThread.run();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }





}
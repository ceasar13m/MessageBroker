package com.ainur;

import com.ainur.servlets.ChannelServlet;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;


public class ConnectionReceiver {
    private MessageProcessor processor;
    private String host = "localhost";
    private int port = 8080;

    public static void main(String[] args) {
        ConnectionReceiver server = new ConnectionReceiver();
        server.start();
    }


    public ConnectionReceiver() {
        processor = new MessageProcessor();
        processor.startWorkers();
        TokensStorage.getTokenStorage();
    }

    public void start() {
        try {
            WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(host, port)) {
                @Override
                public void onOpen(WebSocket conn, ClientHandshake handshake) {

                }

                @Override
                public void onClose(WebSocket conn, int code, String reason, boolean remote) {

                }

                @Override
                public void onMessage(WebSocket conn, String message) {

                }

                @Override
                public void onError(WebSocket conn, Exception ex) {

                }
            };
            webSocketServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

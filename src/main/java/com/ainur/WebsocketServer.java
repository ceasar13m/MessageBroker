package com.ainur;

import com.ainur.model.messages.Message;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import java.net.InetSocketAddress;
import java.util.logging.Logger;



public class WebsocketServer {
    private Logger log;
    private MessageProcessor processor;
    @Autowired
    private Gson gson;

    private final String HOST = "localhost";
    private final int PORT = 8090;


    public WebsocketServer() {
        processor = new MessageProcessor();
        processor.startWorkers();
        TokensStorage.getTokenStorage();
        this.log  = Logger.getLogger(WebsocketServer.class.getName());
    }

    public void start() {
        log.info("Сокет сервер запущен");
        try {
            WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress(HOST, PORT)) {
                @Override
                public void onOpen(WebSocket conn, ClientHandshake handshake) {
                    log.info("Соединение установлено с "	+ conn.getRemoteSocketAddress());
                }

                @Override
                public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                    log.info("Соединение закрыто");
                }

                @Override
                public void onMessage(WebSocket conn, String jsonMessage) {
                    log.info("received message from "	+ conn.getRemoteSocketAddress() + ": " );
                    Message message = gson.fromJson(jsonMessage, Message.class);
                    processor.addMessage(message, conn);
                }

                @Override
                public void onError(WebSocket conn, Exception ex) {
                    log.info("Ошибка: " + ex);
                }
            };
            webSocketServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

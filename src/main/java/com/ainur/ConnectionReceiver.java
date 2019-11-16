package com.ainur;

import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ConnectionReceiver {
    private MessageProcessor processor;
    private ServerSocket serverSocket;
    private String host = "localhost";
    private int port = 8080;

    public static void main(String[] args) {
        ConnectionReceiver server = new ConnectionReceiver();
        server.start();
    }


    public ConnectionReceiver() {
        processor = new MessageProcessor(WebSocketsStorage.getWebSocketsStorage());
        processor.startWorkers();
        TokensStorage.getTokenStorage();
    }

    public void start() {



                WebSocketServer server = new Server(new InetSocketAddress(host, port));
                server.run();
        System.out.println();
//
//                while (true) {
////                    Socket clientSocket;
////                    clientSocket = serverSocket.accept();
//                    ClientThread clientThread = new ClientThread(processor, conn);
//                    clientThread.start();
//                }


    }
}

package com.ainur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionReceiver {
    private MessageProcessor processor;
    private ServerSocket serverSocket;
    private SocketsStorage socketsStorage = new SocketsStorage();
    private TokensStorage tokensStorage;


    public ConnectionReceiver() {
        processor = new MessageProcessor(socketsStorage);
        processor.startWorkers();
        tokensStorage.getTokenStorage();
    }

    public void start() {
        try {
            try {
                serverSocket = new ServerSocket(8080);

                while (true) {
                    Socket clientSocket;
                    clientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(processor, clientSocket);
                    clientThread.start();
                }
            } finally {
                serverSocket.close();
                System.out.println("Сервер закрыт...");
            }
        } catch (IOException e) {
            System.err.println(e + "error");
        }

    }
}

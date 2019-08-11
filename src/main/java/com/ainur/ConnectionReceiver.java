package com.ainur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionReceiver {
    private MessageProcessor processor;
    private ServerSocket serverSocket;
    private SocketsStorage socketsStorage = new SocketsStorage();

    public ConnectionReceiver() {
        processor = new MessageProcessor(socketsStorage);
        processor.startWorkers();
    }

    public void start() throws IOException {
        while (true) {
            Socket clientSocket;
            clientSocket = serverSocket.accept();
            ClientThread clientThread = new ClientThread(processor, clientSocket);
            clientThread.start();
        }

    }
}

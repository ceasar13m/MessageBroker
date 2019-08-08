package com.ainur;

import com.ainur.MessageProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiver extends Thread {

    private Socket clientSocket;
    private ServerSocket serverSocket;


    public void run() {
        System.out.println("Запуск сервера...");

        try {
            try {
                serverSocket = new ServerSocket(8080);

                System.out.println("Сервер запущен...");

                while (true) {
                    System.out.println("Сервер ожидает нового клиента..");
                    clientSocket = serverSocket.accept();

                    System.out.println("Новый клиент..выделение потока..");
                    MessageProcessor processor = new MessageProcessor(clientSocket);
                    processor.start();
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

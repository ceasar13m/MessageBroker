package com.ainur;

import com.ainur.model.Message;

import java.io.*;
import java.net.Socket;

/**
 * Ожидает авторизацию клиента и, после получения, уничтожается
 */
public class ClientThread extends Thread {

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    MessageProcessor processor;

    public ClientThread(MessageProcessor processor, Socket socket) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String jsonMessage = null;
            while (true) {
                jsonMessage = reader.readLine();
                String command = "";
                //Отпарсить сообщение

                processor.addMessage(new Message(), socket);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.ainur;

import com.ainur.model.messages.*;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * blockingQueue <- message
 * 10 обработчиков обрабаотывают сообщения
 */

public class MessageProcessor {

    private BlockingQueue<Message> messages;
    private ArrayList<Worker> workers = new ArrayList<>();
    @Autowired
    private Gson gson;
    private Logger log;


    public MessageProcessor() {
        TokensStorage.getTokenStorage();
        messages = new ArrayBlockingQueue<>(1024);
        this.log = Logger.getLogger(MessageProcessor.class.getName());
        log.info("Конструктор процессора");
    }


    public void addMessage(Message message, WebSocket socket) {
        log.info("Метод addMessage");
        if (message.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(message.getToken())) {
            WebSocketsStorage.getWebSocketsStorage().addSocket(
                    TokensStorage.getTokenStorage().getUserId(message.getToken()),
                    socket);
            messages.add(message);
            socket.send("OK");
        } else {
            socket.send("NO");
        }

    }

    public void startWorkers() {
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(messages);
            workers.add(worker);
            worker.start();
            log.info("worker " + i + " запущен");
        }
    }

    public void stopWorkers() {
        for (Worker worker : workers) {
            worker.stopWorker();
        }
    }

}

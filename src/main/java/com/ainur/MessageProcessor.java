package com.ainur;

import com.ainur.model.messages.*;
import com.ainur.model.responses.AuthResponse;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * blockingQueue <- message
 * 10 обработчиков обрабаотывают сообщения
 */

@Component
public class MessageProcessor {

    private BlockingQueue<Message> messages;
    private BlockingQueue<AuthMessage> authMessages;
    private ArrayList<Worker> workers = new ArrayList<>();
    private Gson gson;


    public MessageProcessor() {
        TokensStorage.getTokenStorage();
        gson = new Gson();
        messages = new ArrayBlockingQueue<>(1024);
        authMessages = new ArrayBlockingQueue<>(1024);
    }


    public void addMessage(Message message, WebSocket socket) {

        switch (message.getCommand()) {
            case MessageType.PUBLISH: {
                PublishMessage publishMessage = gson.fromJson(message.getData(), PublishMessage.class);

                if (publishMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(publishMessage.getToken())) {
                    WebSocketsStorage.getWebSocketsStorage().addSocket(
                            TokensStorage.getTokenStorage().getUserId(publishMessage.getToken()),
                            socket);
                    messages.add(message);
                    socket.send("OK");
                } else {
                    socket.send("NO");
                }
                break;
            }
            case MessageType.SUBSCRIBE: {
                SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
                if (subscribeMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(subscribeMessage.getToken())) {
                    WebSocketsStorage.getWebSocketsStorage().addSocket(
                            TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken()),
                            socket);
                    messages.add(message);
                    socket.send("OK");
                } else {
                    socket.send("NO");

                }
                break;
            }
            case MessageType.CREATE_CHANNEL: {
                CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
                if (createChannelMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(createChannelMessage.getToken())) {
                    WebSocketsStorage.getWebSocketsStorage().addSocket(
                            TokensStorage.getTokenStorage().getUserId(createChannelMessage.getToken()),
                            socket);
                    messages.add(message);
                    socket.send("OK");
                } else {
                    socket.send("NO");

                }
                break;
            }
        }


    }

    public void startWorkers() {
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(messages);
            workers.add(worker);
            worker.start();
        }
    }

    public void stopWorkers() {
        for (Worker worker : workers) {
            worker.stopWorker();
        }
    }

}

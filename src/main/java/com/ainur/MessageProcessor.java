package com.ainur;

import com.ainur.model.messages.*;
import com.ainur.model.responses.StatusResponse;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * blockingQueue <- message
 * 10 обработчиков обрабаотывают сообщения
 */
public class MessageProcessor {

    private BlockingQueue<Message> messages;
    private BlockingQueue<AuthMessage> authMessages;
    private AuthWorker auth;
    private ArrayList<Worker> workers = new ArrayList<>();
    private SocketsStorage socketsStorage;
    private Gson gson;
    private ClassPathXmlApplicationContext context;


    public MessageProcessor(SocketsStorage socketsStorage) {
        TokensStorage.getTokenStorage();
        gson = new Gson();
        this.socketsStorage = socketsStorage;
        messages = new ArrayBlockingQueue<Message>(1024);
        authMessages = new ArrayBlockingQueue<AuthMessage>(1024);
    }


    public void addMessage(Message message, Socket socket) {

        switch (message.getCommand()) {
            case MessageType.SIGN_IN: {
                AuthMessage authMessage = new AuthMessage(message, socket);
                authMessages.add(authMessage);
                break;
            }
            case MessageType.SIGN_UP: {
                AuthMessage authMessage = new AuthMessage(message, socket);
                authMessages.add(authMessage);
                break;
            }
            case MessageType.PUBLISH: {
                PublishMessage publishMessage = gson.fromJson(message.getData(), PublishMessage.class);

                if (publishMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(publishMessage.getToken())) {
                    messages.add(message);
                } else {
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        StatusResponse statusResponse = new StatusResponse();
                        statusResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                        writer.write(gson.toJson(statusResponse, StatusResponse.class) + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case MessageType.SUBSCRIBE: {
                SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
                if (subscribeMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(subscribeMessage.getToken())) {
                    messages.add(message);
                } else {
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        StatusResponse statusResponse = new StatusResponse();
                        statusResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                        writer.write(gson.toJson(statusResponse, StatusResponse.class) + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case MessageType.DISCONNECT: {
                AuthMessage authMessage = new AuthMessage(message, socket);
                DisconnectMessage disconnectMessage = gson.fromJson(message.getData(), DisconnectMessage.class);
                if (disconnectMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(disconnectMessage.getToken())) {
                    authMessages.add(authMessage);
                } else {
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        StatusResponse statusResponse = new StatusResponse();
                        statusResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                        writer.write(gson.toJson(statusResponse, StatusResponse.class) + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case MessageType.CREATE_CHANNEL: {
                CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
                if (createChannelMessage.getToken() != null && TokensStorage.getTokenStorage().isTokenValid(createChannelMessage.getToken())) {
                    messages.add(message);
                } else {
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        StatusResponse statusResponse = new StatusResponse();
                        statusResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                        writer.write(gson.toJson(statusResponse, StatusResponse.class) + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }


    }

    public void startWorkers() {
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(socketsStorage, messages);
            workers.add(worker);
            worker.start();
        }
        auth = new AuthWorker(socketsStorage, authMessages);
        auth.start();

    }

    public void stopWorkers() {
        for (Worker worker : workers) {
            worker.stopWorker();
        }
    }

}

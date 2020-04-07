package com.ainur;

import com.ainur.model.messages.CreateChannelMessage;
import com.ainur.model.messages.Message;
import com.ainur.model.messages.PublishMessage;
import com.ainur.model.messages.SubscribeMessage;
import com.ainur.model.responses.Token;
import com.ainur.repository.MySQLRepository;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.BlockingQueue;


public class Worker extends Thread {

    private BlockingQueue<Message> messages;
    private Gson gson;
    @Autowired
    MySQLRepository mySQLRepository;


    public Worker(BlockingQueue<Message> messages) {
        this.messages = messages;
        gson = new Gson();
    }


    /**
     *
     */
    @Override
    public void run() {
        while (true) {
            try {
                Message message = messages.take();
                switch (message.getCommand()) {
                    case MessageType.PUBLISH: {
                        publish(message);
                        break;
                    }

                    case MessageType.SUBSCRIBE: {
                        subscribe(message);
                        break;
                    }

                    case MessageType.CREATE_CHANNEL: {
                        createChannel(message);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }


    private void publish(Message message) {
        PublishMessage publishMessage = gson.fromJson(message.getData(), PublishMessage.class);
        mySQLRepository.addMessage(publishMessage);
    }

    private void subscribe(Message message) {
        SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
        mySQLRepository.subscribe(subscribeMessage);
    }

    private void createChannel(Message message) {
        CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
        Token token = new Token();
        token.setToken(message.getToken());
        mySQLRepository.createChannel(createChannelMessage, token);
    }
}

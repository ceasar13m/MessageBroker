package com.ainur;

import com.ainur.model.Message;
import com.ainur.util.MessageType;
import com.google.gson.Gson;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
    private SocketsStorage socketsStorage;
    private BlockingQueue<Message> messages;
    private Gson gson;

    public Worker(SocketsStorage socketsStorage, BlockingQueue<Message> messages) {
        this.messages = messages;
        this.socketsStorage = socketsStorage;
    }
    /**
     *
     */
    @Override
    public void run() {
        while(true) {
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
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }


    private void publish(Message message) {

    }

    private void subscribe(Message message) {

    }


}

package com.ainur;

import com.ainur.model.Message;
import com.ainur.model.PublishMessage;
import com.ainur.model.SubscribeMessage;
import com.ainur.util.MessageType;
import com.google.gson.Gson;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
    SocketsStorage socketsStorage;
    BlockingQueue<Message> messages;
    Gson gson;

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
                if(message.getCommand() == MessageType.SUBSCRIBE) {
                    System.out.println(message.getData());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }


}

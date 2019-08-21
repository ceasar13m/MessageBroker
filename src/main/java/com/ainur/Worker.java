package com.ainur;

import com.ainur.model.Message;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
    SocketsStorage socketsStorage;
    BlockingQueue<Message> messages;

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
                System.out.println(message.getPassword());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }
}

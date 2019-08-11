package com.ainur;

import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
    SocketsStorage socketsStorage;
    BlockingQueue messageStorage;

    public Worker(SocketsStorage socketsStorage, BlockingQueue messageStorage) {
        this.messageStorage = messageStorage;
        this.socketsStorage = socketsStorage;
    }
    /**
     *
     */
    @Override
    public void run() {
        while(true) {
            try {
                messageStorage.take();
                //
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }
}

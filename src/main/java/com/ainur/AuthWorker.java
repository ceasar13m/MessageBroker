package com.ainur;

import com.ainur.model.AuthMessage;
import com.ainur.model.Message;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class AuthWorker extends Thread{
    SocketsStorage socketsStorage;
    BlockingQueue<AuthMessage> authMessages;

    public AuthWorker(SocketsStorage socketsStorage, BlockingQueue<AuthMessage> authMessages) {
        this.socketsStorage = socketsStorage;
        this.authMessages = authMessages;
    }

    public void signIn(Message message, Socket socket) {

    }

    public void signUp(Message message, Socket socket) {

    }


    @Override
    public  void run() {
        while(true) {
            try {
                AuthMessage message = authMessages.take();


                //signIn() or signUp()
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

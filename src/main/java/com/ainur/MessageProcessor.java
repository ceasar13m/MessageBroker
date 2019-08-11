package com.ainur;

import com.ainur.model.AuthMessage;
import com.ainur.model.Message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * blockingQueue <- message
 * 10 обработчиков обрабаотывают сообщения
 */
public class MessageProcessor  {
    SocketsStorage socketsStorage;
    public MessageProcessor(SocketsStorage socketsStorage) {
        this.socketsStorage = socketsStorage;
    }




    BlockingQueue<Message> messages;
    BlockingQueue<AuthMessage> authMessages;
    AuthWorker auth;



    ArrayList<Worker> workers = new ArrayList<>();

    public void addMessage(Message message, Socket socket) {
       if(message.getCommand().equals("SgnIn or signUp")) {
           AuthMessage authMessage = new AuthMessage(message, socket);
           authMessages.add(authMessage);
       }
       else
           messages.add(message);
    }

    public  void startWorkers() {
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker(socketsStorage, messages);
            workers.add(worker);
            worker.start();
        }
        auth = new AuthWorker(socketsStorage, authMessages);
        auth.start();

    }

    public  void stopWorkers() {
        for (Worker worker: workers) {
            worker.stopWorker();
        }
    }

}

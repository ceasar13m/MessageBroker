package com.ainur;

import com.ainur.model.messages.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessagesStorage {
    private BlockingQueue<Message> messages;
    private static MessagesStorage messagesStorage;

    private MessagesStorage() {
        messages = new ArrayBlockingQueue<>(1024);
    }

    public Message takeMessage() throws InterruptedException {
        return messages.take();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public BlockingQueue<Message> getMessages() {
        return messages;
    }

    public static MessagesStorage getMessagesStorage() {
        if (messagesStorage == null)
            messagesStorage = new MessagesStorage();
        return messagesStorage;
    }

}

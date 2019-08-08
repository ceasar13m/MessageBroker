package com.ainur;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public interface Mediator {
    void addClient(Socket socket);
    void removeClient(Socket socket);
    void notifyClients();
}

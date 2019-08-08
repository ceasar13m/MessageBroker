package com.ainur;

import org.springframework.beans.factory.annotation.Autowired;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;


public class Broker implements Mediator {
    @Autowired
    ConcurrentHashMap <Socket, Socket> sockets;

    public void addClient(Socket socket) {
        sockets.put(socket, socket);
    }

    public void removeClient(Socket socket) {
        sockets.remove(socket);
    }

    public void notifyClients() {

    }
}

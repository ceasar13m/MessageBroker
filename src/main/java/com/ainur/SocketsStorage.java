package com.ainur;


import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class SocketsStorage  {
    private ConcurrentHashMap <String, Socket> sockets = new ConcurrentHashMap<String, Socket>();
    public void addClient(String id, Socket socket) {
        sockets.put(id, socket);
    }
    public Socket getClient(String id) {
        return sockets.get(id);
    }
    public void removeClient(String id) {
        sockets.remove(id);
    }
    public void notifyClients() {

    }
}

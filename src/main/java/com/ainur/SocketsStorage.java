package com.ainur;


import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class SocketsStorage {
    private ConcurrentHashMap<String, Socket> sockets;
    private static SocketsStorage socketsStorage;


    private SocketsStorage() {
        sockets = new ConcurrentHashMap<>();
    }

    public static SocketsStorage getSocketsStorage() {
        if (socketsStorage == null) {
            socketsStorage = new SocketsStorage();
        }
        return socketsStorage;
    }


    public boolean addSocket(String id, Socket socket) {
        if (sockets.containsKey(id))
            return false;
        sockets.put(id, socket);
        return true;
    }

    public Socket getSocket(String id) {
        return sockets.get(id);

    }

}

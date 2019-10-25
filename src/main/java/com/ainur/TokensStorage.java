package com.ainur;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class TokensStorage {

    private Socket socket = new Socket();

    private ConcurrentHashMap<String, Socket> tokens;
    private static TokensStorage tokensStorage;

    private TokensStorage() {
        tokens = new ConcurrentHashMap<>();
    }

    public static synchronized TokensStorage getTokenStorage() {
        if (tokensStorage == null) {
            tokensStorage = new TokensStorage();
        }
        return tokensStorage;
    }


    public boolean isTokenValid(String token) {
        return tokens.containsKey(token);
    }

    public synchronized boolean addToken(String token, Socket socket) {
        if (tokens.containsKey(token))
            return false;
        tokens.put(token, socket);
        return true;
    }

    public synchronized void removeToken(String token) {
        tokens.remove(token);
    }
}
package com.ainur;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class TokensStorage {


    private ConcurrentHashMap<String, String> tokens;
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


    public synchronized boolean isTokenValid(String token) {
        return tokens.containsKey(token);
    }

    public synchronized boolean addToken(String token, String id) {
        if (tokens.containsKey(token))
            return false;
        tokens.put(token, id);
        return true;
    }

    public synchronized void removeToken(String token) {
        tokens.remove(token);
    }

    public synchronized String getUserId(String token) {
        return tokens.get(token);
    }
}
package com.ainur;

import java.util.concurrent.ConcurrentHashMap;

public class TokensStorage {

    private Object object = new Object();

    private ConcurrentHashMap<String, Object> tokens;
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

    public boolean addToken(String token) {
        if (tokens.containsKey(token))
            return false;
        tokens.put(token, object);
        return true;
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}
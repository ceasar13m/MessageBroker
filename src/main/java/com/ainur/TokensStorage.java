package com.ainur;

import java.util.concurrent.ConcurrentHashMap;

public class TokensStorage {

    private Object object = new Object();

    private ConcurrentHashMap<String, Object> tokensStorage = new ConcurrentHashMap<>();


    public boolean isTokenValid(String token) {
        return tokensStorage.containsKey(token);
    }

    public boolean addToken(String token) {
        if (tokensStorage.containsKey(token))
            return false;
        tokensStorage.put(token, object);
        return true;
    }

    public void removeToken(String token) {

        tokensStorage.remove(token);

    }
}
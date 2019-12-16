package com.ainur;

import java.util.concurrent.ConcurrentHashMap;

public class TokensStorage {


    private ConcurrentHashMap<String, String> tokens;
    private static TokensStorage tokensStorage;

    private TokensStorage() {
        tokens = new ConcurrentHashMap<>();
    }

    public static TokensStorage getTokenStorage() {
        if (tokensStorage == null) {
            tokensStorage = new TokensStorage();
        }
        return tokensStorage;
    }


    public boolean isTokenValid(String token) {
        return tokens.containsKey(token);
    }

    public boolean addToken(String token, String id) {
        if (tokens.containsKey(token))
            return false;
        tokens.put(token, id);
        return true;
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public String getUserId(String token) {
        return tokens.get(token);
    }
}
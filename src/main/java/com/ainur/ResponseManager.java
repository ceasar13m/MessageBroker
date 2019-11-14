package com.ainur;

import com.ainur.model.responses.StatusResponse;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;


public class ResponseManager {
    private Gson gson;

    public ResponseManager(int httpStatusCode, WebSocket socket) {
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            socket.send(stringResponse);
    }


    public ResponseManager(int httpStatusCode, WebSocket socket, String token) {
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            response.setToken(token);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            socket.send(stringResponse);

    }
}

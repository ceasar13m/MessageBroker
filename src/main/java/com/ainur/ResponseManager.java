package com.ainur;

import com.ainur.model.responses.StatusResponse;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ResponseManager {
    private BufferedWriter writer;
    private Gson gson;

    public ResponseManager(int httpStatusCode, Socket socket) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            writer.write(stringResponse);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ResponseManager(int httpStatusCode, Socket socket, String token) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            response.setToken(token);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            writer.write(stringResponse);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.ainur;

import com.ainur.model.responses.StatusResponse;
import com.ainur.util.HttpStatus;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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


    public ResponseManager(int httpStatusCode, HttpServletResponse resp) {
        try {
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            resp.setContentType("application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.setStatus(HttpStatus.OK);
            resp.getWriter().println(stringResponse);
            resp.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ResponseManager(int httpStatusCode, HttpServletResponse resp, String dataString) {
        try {
            gson = new Gson();
            StatusResponse response = new StatusResponse();
            response.setStatusCode(httpStatusCode);
            response.setToken(dataString);
            String stringResponse = gson.toJson(response, StatusResponse.class) + "\n";
            resp.setContentType("application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.setStatus(HttpStatus.OK);
            resp.getWriter().println(stringResponse);
            resp.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.ainur.servlets;

import com.ainur.ResponseManager;
import com.ainur.SQLWorker;
import com.ainur.TokensStorage;
import com.ainur.WebSocketsStorage;
import com.ainur.model.messages.Message;
import com.ainur.model.messages.SignInMessage;
import com.ainur.util.HttpStatus;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class AuthServlet extends HttpServlet {
    Gson gson = new Gson();
    SQLWorker sqlWorker = new SQLWorker();
    UUID uuid;


    /**
     * SignIn
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SignInMessage signInMessage = gson.fromJson(req.getReader(), SignInMessage.class);
        if (sqlWorker.isLoginPasswordValid(signInMessage.getUsername(), signInMessage.getPassword())) {
            uuid = UUID.randomUUID();
            new ResponseManager(HttpStatus.OK, resp, uuid.toString());
            TokensStorage.getTokenStorage().addToken(uuid.toString(), sqlWorker.getUserId(signInMessage.getUsername()));
//            WebSocketsStorage.getWebSocketsStorage().addSocket(sqlWorker.getUserId(signInMessage.getUsername()), socket);
        } else {
            new ResponseManager(HttpStatus.UNAUTHORIZED, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}

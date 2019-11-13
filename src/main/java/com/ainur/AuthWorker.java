package com.ainur;

import com.ainur.model.messages.*;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;

import java.net.Socket;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class AuthWorker extends Thread {
    private SocketsStorage socketsStorage;
    private BlockingQueue<AuthMessage> authMessages;
    private Gson gson;
    private UUID uuid;
    DBRequest dbRequest;


    public AuthWorker(SocketsStorage socketsStorage, BlockingQueue<AuthMessage> authMessages) {
        this.socketsStorage = socketsStorage;
        this.authMessages = authMessages;
        gson = new Gson();
        dbRequest = new DBRequest();
    }

    public void signIn(Message message, Socket socket) {
        SignInMessage signInMessage = gson.fromJson(message.getData(), SignInMessage.class);
        if (isLoginPasswordValid(signInMessage.getUsername(), signInMessage.getPassword())) {
            uuid = UUID.randomUUID();
            new ResponseManager(HttpStatus.OK, socket, uuid.toString());
            TokensStorage.getTokenStorage().addToken(uuid.toString(), getUserId(signInMessage.getUsername()));
            SocketsStorage.getSocketsStorage().addSocket(getUserId(signInMessage.getUsername()), socket);
        } else {
            new ResponseManager(HttpStatus.UNAUTHORIZED, socket);
        }


    }


    public void signUp(Message message, Socket socket) {
        SignUpMessage signUpMessage = gson.fromJson(message.getData(), SignUpMessage.class);
            if (!isUserExists(signUpMessage.getUsername())) {
                uuid = UUID.randomUUID();
                String userInsertString = "insert into users (username, password) values ('" + signUpMessage.getUsername() + "','" + signUpMessage.getPassword() + "');";
                dbRequest.dbRequest(userInsertString);
                new ResponseManager(HttpStatus.OK, socket, uuid.toString());
                TokensStorage.getTokenStorage().addToken(uuid.toString(), signUpMessage.getUsername());
            } else {
                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }


    }


    public void disconnect(Message message, Socket socket) {
        DisconnectMessage disconnectMessage = gson.fromJson(message.getData(), DisconnectMessage.class);
        TokensStorage.getTokenStorage().removeToken(disconnectMessage.getToken());
        new ResponseManager(HttpStatus.OK, socket);

    }

    public boolean isUserExists(String login) {
        try {
            String sqlString = "select * from users where username = '" + login + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);

            if (resultSet.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public String getUserId(String username) {
        try {
            String sqlString = "select * from users where username = '" + username + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);

            if (resultSet.next())
                return resultSet.getString(1).toString();
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean isLoginPasswordValid(String login, String password) {
        try {
            String sqlString = "select * from users where username = '" + login + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            if (resultSet.next()) {
                if (resultSet.getString(2).equals(login) && resultSet.getString(3).equals(password))
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public void run() {

        while (true) {
            try {
                AuthMessage authMessage = authMessages.take();
                if (authMessage.getMessage().getCommand() == MessageType.SIGN_IN)
                    signIn(authMessage.getMessage(), authMessage.getSocket());
                else if (authMessage.getMessage().getCommand() == MessageType.SIGN_UP)
                    signUp(authMessage.getMessage(), authMessage.getSocket());
                else if (authMessage.getMessage().getCommand() == MessageType.DISCONNECT)
                    disconnect(authMessage.getMessage(), authMessage.getSocket());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

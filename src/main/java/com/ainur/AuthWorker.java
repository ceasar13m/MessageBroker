package com.ainur;

import com.ainur.model.AuthMessage;
import com.ainur.model.Message;
import com.ainur.model.Response;
import com.ainur.util.HttpStatus;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class AuthWorker extends Thread {
    SocketsStorage socketsStorage;
    BlockingQueue<AuthMessage> authMessages;
    Gson gson;
    BufferedWriter writer;
    BufferedReader reader;

    private static final String URL = "jdbc:mysql://localhost:3306" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp" +
            "&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kazan13m";
    private static Connection connection;

    public AuthWorker(SocketsStorage socketsStorage, BlockingQueue<AuthMessage> authMessages) {
        this.socketsStorage = socketsStorage;
        this.authMessages = authMessages;
        gson = new Gson();
        reader = new BufferedReader(new InputStreamReader(socketsStorage..getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            statement.executeUpdate("create database IF NOT EXISTS authorization ;");
            statement.executeUpdate("use authorization;");

            statement.executeUpdate(
                    "CREATE TABLE if not exists users (" +
                            "    username varchar (30) not null," +
                            "    password varchar (30) not null" +
                            ");");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void signIn(Message message, Socket socket) {
        if (isLoginPasswordValid(message.getUsername(), message.getPassword())) {
            System.out.println("SignIN");
        } else {
            System.out.println("SignNO");

        }
    }




    public void signUp(Message message, Socket socket) {
        if (!isUserExists(message.getUsername())) {
            Statement statement;
            try {
                statement = connection.createStatement();
                statement.executeUpdate("use authorization;");

                String userInsertString = "insert into users (username, password) values ('" + message.getUsername() + "','" + message.getPassword() + "');";
                statement.executeUpdate(userInsertString);

                UUID uuid = UUID.randomUUID();

                Response response = createResponse(HttpStatus.OK, uuid.toString());

                String stringResponse = gson.toJson(response, Response.class);
                stringResponse += "\n";
                writer.write(stringResponse);
                writer.flush();


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {


        }
    }

    public boolean isUserExists(String login) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("use authorization;");

            String tempString = "select * from users where username = '" + login + "'";
            ResultSet resultSet = statement.executeQuery(tempString);

            if (resultSet.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }


    public boolean isLoginPasswordValid(String login, String password) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("use authorization ");
            String tempString = "select * from users where username = '" + login + "'";
            ResultSet resultSet = statement.executeQuery(tempString);
            if(resultSet.next()) {
                if(resultSet.getString(1).equals(login) && resultSet.getString(2).equals(password))
                    return true;
                else
                    return false;
            }
            else
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
                if (authMessage.getMessage().getCommand().equals("signIn"))
                    signIn(authMessage.getMessage(), authMessage.getSocket());
                else
                    signUp(authMessage.getMessage(), authMessage.getSocket());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static Response createResponse(int code, String message) {
        Response response = new Response();
        response.code = code;
        response.message = message;

        return response;
    }
}

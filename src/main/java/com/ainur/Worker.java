package com.ainur;

import com.ainur.model.CreateChannelMessage;
import com.ainur.model.Message;
import com.ainur.model.StatusResponse;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
    private BufferedWriter writer;
    private BufferedReader reader;
    private SocketsStorage socketsStorage;
    private BlockingQueue<Message> messages;
    private Gson gson;
    private static Connection connection;




    private static final String URL = "jdbc:mysql://localhost:3306" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp" +
            "&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kazan13m";



    public Worker(SocketsStorage socketsStorage, BlockingQueue<Message> messages) {
        this.messages = messages;
        this.socketsStorage = socketsStorage;

        gson = new Gson();


        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            statement.executeUpdate("create database IF NOT EXISTS authorization;");
            statement.executeUpdate("use authorization;");

            statement.executeUpdate(
                    "CREATE TABLE if not exists channels (" +
                            "    id int AUTO_INCREMENT not null PRIMARY KEY," +
                            "    channelName varchar (30) not null," +
                            ");");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     */
    @Override
    public void run() {
        while(true) {
            try {
                Message message = messages.take();
                switch (message.getCommand()) {
                    case MessageType.PUBLISH: {
                        publish(message);
                        break;
                    }

                    case MessageType.SUBSCRIBE: {
                        subscribe(message);
                        break;
                    }

                    case MessageType.CREATECHANNEL: {
                        createChannel(message);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopWorker() {

    }


    private void publish(Message message) {

    }

    private void subscribe(Message message) {

    }

    private void createChannel(Message message) {

        CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
        Statement statement = null;
        String userId = TokensStorage.getTokenStorage().getUserId(createChannelMessage.getToken());
        Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);


        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            if (TokensStorage.getTokenStorage().isTokenValid(createChannelMessage.getToken())) {

                try {


                    statement = connection.createStatement();
                    statement.executeUpdate("use authorization;");
                    String userInsertString = "insert into channels (channelName) values ('" + createChannelMessage.getChanel() + "');";
                    statement.executeUpdate(userInsertString);

                    String tempString = "select * from channels where channelName = '" + createChannelMessage.getChanel() + "'";
                    ResultSet resultSet = statement.executeQuery(tempString);
                    if (resultSet.next()) {
                        StatusResponse statusResponse = createResponse(HttpStatus.OK, null);
                        String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                        writer.write(stringResponse);
                        writer.flush();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private static StatusResponse createResponse(int code, String token) {
        StatusResponse response = new StatusResponse();
        response.setCode(code);
        response.setToken(token);

        return response;
    }


}

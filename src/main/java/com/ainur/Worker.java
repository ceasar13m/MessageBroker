package com.ainur;

import com.ainur.model.messages.CreateChannelMessage;
import com.ainur.model.messages.Message;
import com.ainur.model.messages.PublishMessage;
import com.ainur.model.messages.SubscribeMessage;
import com.ainur.model.responses.StatusResponse;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread {
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

            statement.executeUpdate("create database IF NOT EXISTS broker;");
            statement.executeUpdate("use broker;");

            statement.executeUpdate(
                    "CREATE TABLE if not exists channels (" +
                            "    id int AUTO_INCREMENT not null PRIMARY KEY," +
                            "    channel_name varchar (30) not null" +
                            ");");
            statement.executeUpdate(
                    "CREATE TABLE if not exists subscriptions (" +
                            "    subscriber int not null," +
                            "    channel int not null," +
                            "    FOREIGN KEY (subscriber) REFERENCES users(id), " +
                            "    FOREIGN KEY (channel) REFERENCES channels(id) " +
                            ");");
            statement.executeUpdate(
                    "CREATE TABLE if not exists messages (" +
                            "    id int AUTO_INCREMENT NOT NULL  PRIMARY KEY," +
                            "    send_date date not null," +
                            "    message TEXT not null," +
                            "    sender int not null," +
                            "    channel int not null," +
                            "    FOREIGN KEY (sender) REFERENCES users(id), " +
                            "    FOREIGN KEY (channel) REFERENCES channels(id) " +
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
        while (true) {
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

                    case MessageType.CREATE_CHANNEL: {
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
        PublishMessage publishMessage = gson.fromJson(message.getData(), PublishMessage.class);
        Statement statement = null;
        String userId = TokensStorage.getTokenStorage().getUserId(publishMessage.getToken());

        try {
            Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            statement = connection.createStatement();
            statement.executeUpdate("use broker;");

            String tempString = "select * from channels where channel_name = '" + publishMessage.getChannelName() + "'";
            ResultSet resultSet = statement.executeQuery(tempString);
            int channelId;

            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                String messageInsertString = "insert into messages (sender, channel, send_date, message) values ('"
                        + userId + "','"
                        + channelId + "','"
                        + publishMessage.getDateString() + "','"
                        + publishMessage.getMessage() + "');";
                statement.executeUpdate(messageInsertString);
                StatusResponse statusResponse = createResponse(HttpStatus.OK);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();

            } else {
                StatusResponse statusResponse = createResponse(HttpStatus.FORBIDDEN);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void subscribe(Message message) {
        SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
        Statement statement = null;
        String userId = TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken());

        try {
            Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            statement = connection.createStatement();
            statement.executeUpdate("use broker;");

            String tempString = "select * from channels where channel_name = '" + subscribeMessage.getChannelName() + "'";
            ResultSet resultSet = statement.executeQuery(tempString);
            int channelId;

            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                String userInsertString = "insert into subscriptions (subscriber, channel) values ('"
                        + TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken())
                        + "','" + channelId + "');";
                statement.executeUpdate(userInsertString);
                StatusResponse statusResponse = createResponse(HttpStatus.OK);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();

            } else {
                StatusResponse statusResponse = createResponse(HttpStatus.FORBIDDEN);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createChannel(Message message) {
        CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
        Statement statement = null;
        String userId = TokensStorage.getTokenStorage().getUserId(createChannelMessage.getToken());


        try {

            Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            statement = connection.createStatement();
            statement.executeUpdate("use broker;");
            String userInsertString = "insert into channels (channel_name) values ('" + createChannelMessage.getChannelName() + "');";
            statement.executeUpdate(userInsertString);

            String tempString = "select * from channels where channel_name = '" + createChannelMessage.getChannelName() + "'";
            ResultSet resultSet = statement.executeQuery(tempString);
            if (resultSet.next()) {
                StatusResponse statusResponse = createResponse(HttpStatus.OK);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();
            } else {
                StatusResponse statusResponse = createResponse(HttpStatus.FORBIDDEN);
                String stringResponse = gson.toJson(statusResponse, StatusResponse.class) + "\n";
                writer.write(stringResponse);
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private static StatusResponse createResponse(int code) {
        StatusResponse response = new StatusResponse();
        response.setStatusCode(code);

        return response;
    }


}

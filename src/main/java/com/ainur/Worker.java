package com.ainur;

import com.ainur.model.messages.CreateChannelMessage;
import com.ainur.model.messages.Message;
import com.ainur.model.messages.PublishMessage;
import com.ainur.model.messages.SubscribeMessage;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread {

    private SocketsStorage socketsStorage;
    private BlockingQueue<Message> messages;
    private Gson gson;
    DBRequest dbRequest;


    public Worker(SocketsStorage socketsStorage, BlockingQueue<Message> messages) {
        this.messages = messages;
        this.socketsStorage = socketsStorage;
        gson = new Gson();
        dbRequest = new DBRequest();
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
            String sqlString = "select * from channels where channel = '" + publishMessage.getChannelName() + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            int channelId;
            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                System.out.println(publishMessage.getDateString());
                sqlString = "insert into messages (sender_id, channel_id, sent_time, message) values ('"
                        + userId + "','"
                        + channelId + "','"
                        + publishMessage.getDateString() + "','"
                        + publishMessage.getMessage() + "');";
                dbRequest.dbRequest(sqlString);
                new ResponseManager(HttpStatus.OK, socket);
            } else {
                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void subscribe(Message message) {
        SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
        String userId = TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken());
        try {
            Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);
            String sqlString = "select * from channels where channel = '" + subscribeMessage.getChannelName() + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            int channelId;
            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                sqlString = "insert into subscriptions (subscriber_id, channel_id) values ('"
                        + TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken())
                        + "','" + channelId + "');";
                dbRequest.dbRequest(sqlString);
                new ResponseManager(HttpStatus.OK, socket);
            } else {
                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createChannel(Message message) {
        CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
        String userId = TokensStorage.getTokenStorage().getUserId(createChannelMessage.getToken());
        try {
            Socket socket = SocketsStorage.getSocketsStorage().getSocket(userId);
            String sqlString = "insert into channels (channel) values ('" + createChannelMessage.getChannelName() + "');";
            dbRequest.dbRequest(sqlString);

            sqlString = "select * from channels where channel = '" + createChannelMessage.getChannelName() + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            if (resultSet.next()) {
                new ResponseManager(HttpStatus.OK, socket);
            } else {
                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

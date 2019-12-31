package com.ainur;

import com.ainur.model.messages.CreateChannelMessage;
import com.ainur.model.messages.Message;
import com.ainur.model.messages.PublishMessage;
import com.ainur.model.messages.SubscribeMessage;
import com.ainur.repository.MySQLRepository;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread {

    private BlockingQueue<Message> messages;
    private Gson gson;

    @Autowired
    MySQLRepository mySQLRepository;

    @Autowired
    DataSource dataSource;



    public Worker(BlockingQueue<Message> messages) {
        this.messages = messages;
        gson = new Gson();
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
        System.out.println("Publish");
        PublishMessage publishMessage = gson.fromJson(message.getData(), PublishMessage.class);
        MessageBroker broker = new MessageBroker(publishMessage);
        String userId = TokensStorage.getTokenStorage().getUserId(publishMessage.getToken());
        try (Connection connection = dataSource.getConnection()){
            WebSocket socket = WebSocketsStorage.getWebSocketsStorage().getSocket(userId);
            String sql = "select * from channels where channel = '" + publishMessage.getChannelName() + "'";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int channelId;
            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                System.out.println(publishMessage.getDateString());
                sql = "insert into messages (sender_id, channel_id, sent_time, message) values ('"
                        + userId + "','"
                        + channelId + "','"
                        + publishMessage.getDateString() + "','"
                        + publishMessage.getMessage() + "');";
                preparedStatement.executeQuery(sql);
//                new ResponseManager(HttpStatus.OK, socket);
            } else {
//                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void subscribe(Message message) {
        SubscribeMessage subscribeMessage = gson.fromJson(message.getData(), SubscribeMessage.class);
        String userId = TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken());
        try (Connection connection = dataSource.getConnection()){
            WebSocket socket = WebSocketsStorage.getWebSocketsStorage().getSocket(userId);
            String sql= "select * from channels where channel = '" + subscribeMessage.getChannelName() + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            int channelId;
            if (resultSet.next()) {
                channelId = Integer.parseInt(resultSet.getString(1));
                sql = "insert into subscriptions (subscriber_id, channel_id) values ('"
                        + TokensStorage.getTokenStorage().getUserId(subscribeMessage.getToken())
                        + "','" + channelId + "');";
                preparedStatement.executeQuery(sql);
//                new ResponseManager(HttpStatus.OK, socket);
            } else {
//                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createChannel(Message message) {
        System.out.println("helloWorld");
        CreateChannelMessage createChannelMessage = gson.fromJson(message.getData(), CreateChannelMessage.class);
        String userId = TokensStorage.getTokenStorage().getUserId(createChannelMessage.getToken());
        try (Connection connection = dataSource.getConnection()){
            WebSocket socket = WebSocketsStorage.getWebSocketsStorage().getSocket(userId);
            String sql= "insert into channels (channel) values ('" + createChannelMessage.getChannelName() + "');";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            sql = "select * from channels where channel = '" + createChannelMessage.getChannelName() + "'";
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                new ResponseManager(HttpStatus.OK, socket);
            } else {
//                new ResponseManager(HttpStatus.FORBIDDEN, socket);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.ainur;

import com.ainur.model.messages.Ildar;
import com.ainur.model.messages.PublishMessage;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageBroker {
    PublishMessage message;
    DBRequest dbRequest;
    Gson gson;

    public MessageBroker(PublishMessage message) {
        this.message = message;
        dbRequest = new DBRequest();
        gson = new Gson();
    }

    private void SendMessage() {
        Ildar ildar = new Ildar();
        String sender = "noname";

        try {
            String sqlString = "select * from users where id = '" + TokensStorage.getTokenStorage().getUserId(message.getToken()) + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            sender = resultSet.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ildar.setSender(sender);
        ildar.setChannelName(message.getChannelName());
        ildar.setMessage(message.getMessage());
        ildar.setSendDateString(message.getDateString());
        String jsonString = gson.toJson(ildar, Ildar.class);


        try {
            String sqlString = "select * from channels where channel = '" + message.getChannelName() + "'";
            ResultSet resultSet = dbRequest.getResult(sqlString);
            String channelId = resultSet.getString(1);

            sqlString = "select * from subscriptions where channel_id = '" + channelId + "'";
            resultSet = dbRequest.getResult(sqlString);
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                WebSocketsStorage.getWebSocketsStorage().getSocket(id).send(jsonString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

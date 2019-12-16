package com.ainur;

import com.ainur.model.messages.MessagePocket;
import com.ainur.model.messages.PublishMessage;
import com.ainur.repository.MySQLRepository;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;

public class MessageBroker {
    PublishMessage message;
    MySQLRepository mySQLRepository;
    Gson gson;

    public MessageBroker(PublishMessage message) {
        this.message = message;
        mySQLRepository = new MySQLRepository();
        gson = new Gson();
    }

    private void SendMessage() {
        MessagePocket messagePocket = new MessagePocket();
        String sqlString = "select * from users where id = '" + TokensStorage.getTokenStorage().getUserId(message.getToken()) + "'";

        messagePocket.setSender(mySQLRepository.getUserName(sqlString));
        messagePocket.setChannelName(message.getChannelName());
        messagePocket.setMessage(message.getMessage());
        messagePocket.setSendDateString(message.getDateString());
        String jsonString = gson.toJson(messagePocket, MessagePocket.class);


        sqlString = "select * from channels where channel = '" + message.getChannelName() + "'";

        sqlString = "select * from subscriptions where channel_id = '" + mySQLRepository.getChannelId(sqlString) + "'";
        ArrayList<String> subscribersId = mySQLRepository.getSubscribersId(sqlString);
        for (String id : subscribersId) {
            WebSocketsStorage.getWebSocketsStorage().getSocket(id).send(jsonString);
        }

    }
}

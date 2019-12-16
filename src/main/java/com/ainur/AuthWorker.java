package com.ainur;

import com.ainur.model.messages.*;
import com.ainur.repository.MySQLRepository;
import com.ainur.util.HttpStatus;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class AuthWorker extends Thread {
    private WebSocketsStorage webSocketsStorage;
    private BlockingQueue<AuthMessage> authMessages;
    private Gson gson;
    private UUID uuid;
    MySQLRepository dbRequest;


    public AuthWorker(WebSocketsStorage webSocketsStorage, BlockingQueue<AuthMessage> authMessages) {
        this.webSocketsStorage = webSocketsStorage;
        this.authMessages = authMessages;
        gson = new Gson();
        dbRequest = new MySQLRepository();
    }





    public void disconnect(Message message, WebSocket socket) {

        DisconnectMessage disconnectMessage = gson.fromJson(message.getData(), DisconnectMessage.class);
        String userId = TokensStorage.getTokenStorage().getUserId(disconnectMessage.getToken());
        WebSocketsStorage.getWebSocketsStorage().removeSocket(userId);
        TokensStorage.getTokenStorage().removeToken(disconnectMessage.getToken());
        new ResponseManager(HttpStatus.OK, socket);

    }






    @Override
    public void run() {

        while (true) {
            try {
                AuthMessage authMessage = authMessages.take();
                if (authMessage.getMessage().getCommand() == MessageType.DISCONNECT)
                    disconnect(authMessage.getMessage(), authMessage.getSocket());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

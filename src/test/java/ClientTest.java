
import com.ainur.model.*;
import com.ainur.util.MessageType;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class ClientTest {





    @Test
    public void сlient() {
        Gson gson = new Gson();
        String token;


        try {
            Socket clientSocket = new Socket("localhost", 8080);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            Message message = new Message();
            DisconnectMessage disconnectMessage = new DisconnectMessage();


            message.setCommand(MessageType.DISCONNECT);
            message.setData(gson.toJson(disconnectMessage, DisconnectMessage.class));

            String jsonString = gson.toJson(message, Message.class);
            writer.write(jsonString + "\n");
            writer.flush();



            SignInMessage signInMessage = new SignInMessage();
            signInMessage.setUsername("rafil");
            signInMessage.setPassword("1112");

            message.setCommand(MessageType.SIGNIN);
            message.setData(gson.toJson(signInMessage, SignInMessage.class));

            jsonString = gson.toJson(message, Message.class);
            writer.write(jsonString + "\n");
            writer.flush();
            StatusResponse response = gson.fromJson(reader.readLine(), StatusResponse.class);


            SubscribeMessage subscribeMessage = new SubscribeMessage();
            subscribeMessage.setToken(response.getToken());
            subscribeMessage.setChanel("222");
            message.setCommand(MessageType.SUBSCRIBE);
            message.setData(gson.toJson(subscribeMessage, SubscribeMessage.class));

            writer.write(gson.toJson(message, Message.class) + "\n");
            writer.flush();











        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}




import com.ainur.ConnectionReceiver;
import com.ainur.model.Message;
import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class ClientTest {




    @Test
    public void сlient() {
        Gson gson = new Gson();


        try {
            Socket clientSocket = new Socket("localhost", 8080);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                Message message = new Message();

                message.setCommand("signUp");
                message.setUsername("name" );
                message.setPassword("password");

                String jsonString = gson.toJson(message, Message.class);
                writer.write(jsonString + "\n");
                writer.flush();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



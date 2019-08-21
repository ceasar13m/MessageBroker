
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





            for (int i = 0; i < 10; i++) {
                Message message = new Message();

                message.setCommand("commmand " + i);
                message.setUsername("name " + i);
                message.setPassword("password " + i);

                String jsonString = gson.toJson(message, Message.class);
                writer.write(jsonString + "\n");
                writer.flush();
                Thread.sleep(1000);
            }


            String signUpResponseString = reader.readLine();
            System.out.println("Получили от сервера после запроса" + signUpResponseString +"\n");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



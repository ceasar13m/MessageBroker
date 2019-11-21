import com.ainur.ConnectionReceiver;
import org.junit.Test;


public class SocketServer {

    @Test
    public  void startServer() {
        ConnectionReceiver server = new ConnectionReceiver();
        server.start();
    }
}

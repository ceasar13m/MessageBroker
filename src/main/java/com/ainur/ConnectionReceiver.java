package com.ainur;

import com.ainur.servlets.AuthServlet;
import com.ainur.servlets.ChannelServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ConnectionReceiver {
    private MessageProcessor processor;
//    private String host = "localhost";
//    private int port = 8080;

    public static void main(String[] args) {
        ConnectionReceiver server = new ConnectionReceiver();
        server.start();
    }


    public ConnectionReceiver() {
        processor = new MessageProcessor(WebSocketsStorage.getWebSocketsStorage());
        processor.startWorkers();
        TokensStorage.getTokenStorage();
    }

    public void start() {
            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(8000);
            server.setConnectors(new Connector[]{connector});
            ServletHandler servletHandler = new ServletHandler();
            server.setHandler(servletHandler);

            servletHandler.addServletWithMapping(ChannelServlet.class, "/channels");
            servletHandler.addServletWithMapping(AuthServlet.class, "/auth");

        try {
            server.start();
//
//
//            WebSocketServer webSocketServer = new SocketServer(new InetSocketAddress(host, port));
//            webSocketServer.run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

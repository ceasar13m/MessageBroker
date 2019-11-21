package com.ainur;

import com.ainur.servlets.ChannelServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

public class HttpServerStarter {
    public static void main(String [] args) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8088);
        server.setConnectors(new Connector[] {connector});
        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(ChannelServlet.class, "/channels");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.ainur;

import java.sql.*;

public class DBRequest {

    private static final String URL = "jdbc:mysql://localhost:3306" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp" +
            "&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "kazan13m";

    private static Connection connection;
    private Statement statement;


    public DBRequest() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            statement = connection.createStatement();

            statement.executeUpdate("create database IF NOT EXISTS broker;");
            statement.executeUpdate("use broker;");

            statement.executeUpdate(
                    "CREATE TABLE if not exists users (" +
                            "    id int AUTO_INCREMENT not null PRIMARY KEY," +
                            "    username varchar (30) not null," +
                            "    password varchar (30) not null" +
                            ");");

            statement.executeUpdate(
                    "CREATE TABLE if not exists channels (" +
                            "    id int AUTO_INCREMENT not null PRIMARY KEY," +
                            "    channel varchar (30) not null" +
                            ");");
            statement.executeUpdate(
                    "CREATE TABLE if not exists subscriptions (" +
                            "    subscriber_id int not null," +
                            "    channel_id int not null," +
                            "    FOREIGN KEY (subscriber_id) REFERENCES users(id), " +
                            "    FOREIGN KEY (channel_id) REFERENCES channels(id) " +
                            ");");
            statement.executeUpdate(
                    "CREATE TABLE if not exists messages (" +
                            "    id int AUTO_INCREMENT NOT NULL  PRIMARY KEY," +
                            "    sent_time datetime not null," +
                            "    message TEXT not null," +
                            "    sender_id int not null," +
                            "    channel_id int not null," +
                            "    FOREIGN KEY (sender_id) REFERENCES users(id), " +
                            "    FOREIGN KEY (channel_id) REFERENCES channels(id) " +
                            ");");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public ResultSet getResult(String sqlString) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate("use broker;");
            return statement.executeQuery(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    public void dbRequest(String sqlString) {
        try {
            statement = connection.createStatement();
            statement.executeUpdate("use broker;");
            statement.executeUpdate(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

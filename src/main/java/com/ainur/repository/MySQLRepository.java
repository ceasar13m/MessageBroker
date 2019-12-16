package com.ainur.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;


@Repository
public class MySQLRepository {
    private static final String CREATE_DATABASE =
            "create database IF NOT EXISTS broker;";
    private static final String USING_DATABASE =
            "use broker;";
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE if not exists users " +
                    "(id int AUTO_INCREMENT not null PRIMARY KEY, " +
                    "username varchar (30) not null, " +
                    "password varchar (30) not null);";
    private static final String CREATE_CHANNELS_TABLE =
            "CREATE TABLE if not exists channels " +
                    "(id int AUTO_INCREMENT not null PRIMARY KEY," +
                    "channel varchar (30) not null);";
    private static final String CREATE_SUBSCRIPTIONS_TABLE =
            "CREATE TABLE if not exists subscriptions " +
                    "(subscriber_id int not null," +
                    "channel_id int not null," +
                    "FOREIGN KEY (subscriber_id) REFERENCES users(id), " +
                    "FOREIGN KEY (channel_id) REFERENCES channels(id) );";
    private static final String CREATE_MESSAGES_TABLE =
            "CREATE TABLE if not exists messages " +
                    "(id int AUTO_INCREMENT NOT NULL  PRIMARY KEY," +
                    "sent_time datetime not null," +
                    "message TEXT not null," +
                    "sender_id int not null," +
                    "channel_id int not null," +
                    "FOREIGN KEY (sender_id) REFERENCES users(id), " +
                    "FOREIGN KEY (channel_id) REFERENCES channels(id) );";

    @Autowired
    DataSource dataSource;


    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_DATABASE);
            statement.executeUpdate(USING_DATABASE);
            statement.executeUpdate(CREATE_USERS_TABLE);
            statement.executeUpdate(CREATE_CHANNELS_TABLE);
            statement.executeUpdate(CREATE_SUBSCRIPTIONS_TABLE);
            statement.executeUpdate(CREATE_MESSAGES_TABLE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getAllChannels() {
        ArrayList<String> channels = new ArrayList<>();
        String sql = "select * from channels";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                channels.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return channels;
    }


    public boolean isUserExists(String username) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = " select * from users where username = '" + username + "'";
            Statement statement;

            statement = connection.createStatement();
            statement.executeUpdate("use broker");
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return true;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean isLoginPasswordValid(String username, String password) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "select * from users where username = '" + username + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(2).equals(username) && resultSet.getString(3).equals(password))
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public String getUserId(String username) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "select * from users where username = '" + username + "'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                return resultSet.getString(1);
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getUserName(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                return resultSet.getString(1);
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getChannelId(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
                return resultSet.getString(1);
            else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<String> getSubscribersId(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<String> subscribersId = new ArrayList<>();
            while (resultSet.next()) {
                subscribersId.add(resultSet.getString(1));
            }
            return subscribersId;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

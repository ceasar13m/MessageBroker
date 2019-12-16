package com.ainur.controllers;

import com.ainur.ResponseManager;
import com.ainur.TokensStorage;
import com.ainur.model.messages.SignInRequestJson;
import com.ainur.model.messages.SignUpMessage;
import com.ainur.model.responses.StatusResponse;
import com.ainur.repository.MySQLRepository;
import com.ainur.util.HttpStatus;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@RestController

public class AppRESTController {
    @Autowired
    StatusResponse statusResponse;
    @Autowired
    MySQLRepository mySQLRepository;

    @Autowired
    DataSource dataSource;


    Gson gson = new Gson();
    UUID uuid;

    @RequestMapping("/signIn")
    @GetMapping(produces = "application/json")
    public @ResponseBody
    void signIn(HttpServletResponse response, HttpServletRequest request) {
        SignInRequestJson signInRequestJson;
        try {
            signInRequestJson = gson.fromJson(request.getReader(), SignInRequestJson.class);
            if (mySQLRepository.isLoginPasswordValid(signInRequestJson.getUsername(), signInRequestJson.getPassword())) {
                uuid = UUID.randomUUID();
                new ResponseManager(HttpStatus.OK, response, uuid.toString());
                TokensStorage.getTokenStorage().addToken(uuid.toString(), mySQLRepository.getUserId(signInRequestJson.getUsername()));
            } else {
                new ResponseManager(HttpStatus.UNAUTHORIZED, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new ResponseManager(HttpStatus.UNAUTHORIZED, response);
        }

    }



    @RequestMapping("/signUp")
    @PostMapping(produces = "application/json")
    public @ResponseBody
    void signUp(HttpServletResponse response, HttpServletRequest request) {

        try (Connection connection = dataSource.getConnection()){
            SignUpMessage signUpMessage = gson.fromJson(request.getReader(), SignUpMessage.class);
            if (!mySQLRepository.isUserExists(signUpMessage.getUsername())) {
                uuid = UUID.randomUUID();
                String sql = "insert into users (username, password) values ('" + signUpMessage.getUsername() + "','" + signUpMessage.getPassword() + "');";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                new ResponseManager(HttpStatus.OK, response, uuid.toString());
                TokensStorage.getTokenStorage().addToken(uuid.toString(), signUpMessage.getUsername());
            } else {
                new ResponseManager(HttpStatus.UNAUTHORIZED, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
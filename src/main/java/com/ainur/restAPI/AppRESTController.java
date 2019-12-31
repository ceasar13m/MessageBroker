package com.ainur.restAPI;

import com.ainur.TokensStorage;
import com.ainur.model.messages.SignInMessage;
import com.ainur.model.messages.SignUpMessage;
import com.ainur.model.responses.AuthResponse;
import com.ainur.repository.MySQLRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@RestController

public class AppRESTController {
    @Autowired
    AuthResponse authResponse;
    @Autowired
    MySQLRepository mySQLRepository;

    @Autowired
    DataSource dataSource;


    Gson gson = new Gson();
    UUID uuid;

    @RequestMapping("/signIn")
    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<AuthResponse> signIn(HttpServletRequest request) {
        SignInMessage signInMessage;
        try {
            signInMessage = gson.fromJson(request.getReader(), SignInMessage.class);
            if (mySQLRepository.isLoginPasswordValid(signInMessage.getUsername(), signInMessage.getPassword())) {
                uuid = UUID.randomUUID();
                TokensStorage.getTokenStorage().addToken(uuid.toString(), mySQLRepository.getUserId(signInMessage.getUsername()));
                AuthResponse authResponse = new AuthResponse();
                authResponse.setToken(uuid.toString());
                return new ResponseEntity<>(authResponse, HttpStatus.OK);

            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }



    @RequestMapping("/signUp")
    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<AuthResponse> signUp(HttpServletRequest request) {

        try (Connection connection = dataSource.getConnection()) {

            SignUpMessage signUpMessage = gson.fromJson(request.getReader(), SignUpMessage.class);
            if (!mySQLRepository.isUserExists(signUpMessage.getUsername())) {
                System.out.println("good");
                uuid = UUID.randomUUID();
                String sql = "insert into users (username, password) values ('" + signUpMessage.getUsername() + "','" + signUpMessage.getPassword() + "');";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                TokensStorage.getTokenStorage().addToken(uuid.toString(), signUpMessage.getUsername());
                AuthResponse authResponse = new AuthResponse();
                authResponse.setToken(uuid.toString());
                return new ResponseEntity<>(authResponse, org.springframework.http.HttpStatus.OK);
            } else {
                System.out.println("bad");
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


    }

}
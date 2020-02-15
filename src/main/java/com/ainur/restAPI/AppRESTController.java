package com.ainur.restAPI;

import com.ainur.TokensStorage;
import com.ainur.model.messages.SignInMessage;
import com.ainur.model.messages.SignUpMessage;
import com.ainur.model.responses.TokenResponse;
import com.ainur.repository.MySQLRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    ApplicationContext context;
    private TokenResponse tokenResponse;
    private MySQLRepository mySQLRepository;
    private DataSource dataSource;
    private Gson gson;
    private UUID uuid;

    public AppRESTController(DataSource dataSource, MySQLRepository mySQLRepository, TokenResponse tokenResponse) {
        this.mySQLRepository = mySQLRepository;
        this.dataSource = dataSource;
        this.tokenResponse = tokenResponse;
        gson = new Gson();
    }





    @RequestMapping("/signIn")
    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<TokenResponse> signIn(HttpServletRequest request) {
        SignInMessage signInMessage;
        try {
            signInMessage = gson.fromJson(request.getReader(), SignInMessage.class);
            if (mySQLRepository.isLoginPasswordValid(signInMessage.getUsername(), signInMessage.getPassword())) {
                uuid = UUID.randomUUID();
                TokensStorage.getTokenStorage().addToken(uuid.toString(), mySQLRepository.getUserId(signInMessage.getUsername()));
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(uuid.toString());
                return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }



    @RequestMapping("/signUp")
//    @PostMapping(produces = "application/json")
    public @ResponseBody
    ResponseEntity<TokenResponse> signUp(HttpServletRequest request, HttpServletResponse response) {

        try (Connection connection = dataSource.getConnection()) {
            SignUpMessage signUpMessage = gson.fromJson(request.getReader(), SignUpMessage.class);
            if (!mySQLRepository.isUserExists(signUpMessage.getUsername())) {
                uuid = UUID.randomUUID();
                String sql = "insert into users (username, password) values ('" + signUpMessage.getUsername() + "','" + signUpMessage.getPassword() + "');";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
                TokensStorage.getTokenStorage().addToken(uuid.toString(), signUpMessage.getUsername());
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setToken(uuid.toString());
                response.setHeader("Access-Control-Allow-Origin", "*");

                return new ResponseEntity<>(tokenResponse, org.springframework.http.HttpStatus.OK);
            } else {
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
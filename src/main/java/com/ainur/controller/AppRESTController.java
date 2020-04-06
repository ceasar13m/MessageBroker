package com.ainur.controller;

import com.ainur.model.messages.User;
import com.ainur.model.responses.Token;
import com.ainur.repository.MySQLRepository;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
        origins = {"*"}
)
public class AppRESTController {
    @Autowired
    private MySQLRepository mySQLRepository;
    @Autowired
    private DataSource dataSource;
    UUID uuid;

    public AppRESTController() {
    }

    @RequestMapping({"/sign-in"})
    @GetMapping(
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity<Token> signIn(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();

        try {
            User user = (User)gson.fromJson(request.getReader(), User.class);
            Token token = this.mySQLRepository.signIn(user);
            return token.getToken() != null ? new ResponseEntity(token, HttpStatus.OK) : new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (IOException var6) {
            var6.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping({"/sign-up"})
    @PostMapping(
            produces = {"application/json"}
    )
    @ResponseBody
    public ResponseEntity signUp(HttpServletRequest request) {
        Gson gson = new Gson();

        try {
            User user = (User)gson.fromJson(request.getReader(), User.class);
            return this.mySQLRepository.signUp(user) ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (IOException var4) {
            var4.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.sarmad.app.controller;

import com.sarmad.app.exception.InvalidCredentialsException;
import com.sarmad.app.exception.UserAlreadyExistsException;
import com.sarmad.app.model.User;
import com.sarmad.app.service.UserService;
import com.sarmad.app.util.JwtResponse;
import com.sarmad.app.util.LoginRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistsException {
        try {
            System.out.println(user.getFirstName() + " " + user.getSecondName() + " " + user.getLoginId() + " " + user.getPassword());
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully:"+registeredUser.getUserId());
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException("user  already exists with loginId: " + user.getLoginId());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println(loginRequest.getLoginId() + " " + loginRequest.getPassword());
            String token = userService.loginUser(loginRequest.getLoginId(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (InvalidCredentialsException e) {
            throw new InvalidCredentialsException("Check your credentials");
        }
    }
}
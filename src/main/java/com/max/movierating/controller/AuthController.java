package com.max.movierating.controller;

import com.max.movierating.constant.APIConstant;
import com.max.movierating.dto.RequestLoginDTO;
import com.max.movierating.dto.RequestRegisterDTO;
import com.max.movierating.entity.User;
import com.max.movierating.service.impl.AuthServiceImpl;
import com.max.movierating.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * REST controller for authentication requests (login, register, generate token).
 *
 * @author Maxim Semenko
 * @version 1.0
 */
@RestController
@RequestMapping(value = APIConstant.AUTHENTICATION_API)
public class AuthController {

    /**
     * User service for working with user {@link User}.
     */
    private final UserServiceImpl userService;
    /**
     * Authentication service for working with user's login, register, etc.
     */
    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(UserServiceImpl userService, AuthServiceImpl authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Method that is responsible for login of user.
     *
     * @param requestDto request contain username and password
     * @return user and token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody RequestLoginDTO requestDto) {
        return new ResponseEntity<>(authService.login(requestDto), HttpStatus.OK);
    }

    /**
     * Method that logout user from system.
     *
     * @return boolean
     */
    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        return new ResponseEntity<>(authService.logout(), HttpStatus.OK);
    }

    /**
     * Method that is responsible for register of user.
     *
     * @param requestDTO register request
     * @return register user {@link User}
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RequestRegisterDTO requestDTO) {
        return new ResponseEntity<>(userService.save(requestDTO.toUser()), HttpStatus.CREATED);
    }

    /**
     * Method that generates new token for user.
     *
     * @param username user's username
     * @return token {@link String}
     */
    @PostMapping("/token/{username}")
    public ResponseEntity<String> generateNewToken(@PathVariable String username) {
        return new ResponseEntity<>(authService.generateNewToken(username), HttpStatus.OK);
    }

}

package com.chatop.rental.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.chatop.rental.dto.requests.LoginRequest;
import com.chatop.rental.dto.requests.RegisterRequest;
import com.chatop.rental.dto.responses.TokenResponse;
import com.chatop.rental.model.User;
import com.chatop.rental.service.interfaces.AuthService;
import com.chatop.rental.service.interfaces.JwtService;
import com.chatop.rental.service.interfaces.UserService;

@Service
public class AuthServiceImpl implements AuthService{
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenResponse authenticateAndGenerateToken(LoginRequest loginRequest) {
        log.info("Authenticating user {}", loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} authenticated successfully", loginRequest.getEmail());
        return new TokenResponse(jwtService.generateToken(authentication));
    }

    public TokenResponse registerAndGenerateToken(RegisterRequest registerRequest) {
        log.info("Registering user {}", registerRequest.getEmail());
        User newUser = userService.registerUser(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getPassword())
                                  .orElseThrow(() -> {
                                      log.error("Registration failed for {}", registerRequest.getEmail());
                                      return new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
                                  });
        log.info("User {} registered successfully", newUser.getEmail());
        return new TokenResponse(jwtService.generateToken(new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>())));
    }
}
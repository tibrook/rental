package com.chatop.rental.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Implementation of AuthService interface providing authentication and registration functionalities.
 */
@Service
public class AuthServiceImpl implements AuthService{
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthServiceImpl(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
    	this.userService = userService;
    	this.jwtService=jwtService;
    	this.authenticationManager = authenticationManager;
    }
 
    /**
     * Authenticates the user and generates a JWT token.
     * @param loginRequest LoginRequest object containing user's email and password.
     * @return TokenResponse containing the generated JWT token.
     */
    public TokenResponse authenticateAndGenerateToken(LoginRequest loginRequest) {
        log.info("Authenticating user {}", loginRequest.getEmail());
        // Use UserService to authenticate the user first
        userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        // If authentication is successful, proceed with Spring Security Authentication.
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} authenticated successfully", loginRequest.getEmail());
        return new TokenResponse(jwtService.generateToken(authentication));
    }
    /**
     * Registers a new user and generates a JWT token.
     * @param registerRequest RegisterRequest object containing user's email, name, and password.
     * @return TokenResponse containing the generated JWT token.
     * @throws ResponseStatusException if user registration fails due to duplicate email.
     */
    public TokenResponse registerAndGenerateToken(RegisterRequest registerRequest) {
        log.info("Registering user {}", registerRequest.getEmail());
        User newUser = userService.registerUser(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getPassword());
        log.info("User {} registered successfully", newUser.getEmail());
        return new TokenResponse(jwtService.generateToken(new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>())));
    }
}
package com.chatop.rental.controller;

import com.chatop.rental.dto.ErrorResponse;
import com.chatop.rental.dto.LoginRequest;
import com.chatop.rental.dto.RegisterRequest;
import com.chatop.rental.dto.TokenResponse;
import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;
import com.chatop.rental.service.JWTService;
import com.chatop.rental.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final JWTService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Successful registration",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty object", value = "{}")))    
    @PostMapping("/register")
    public TokenResponse registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        log.info("Registering user {}", registerRequest.getEmail());
        
        Optional<User> newUserOptional = userService.registerUser(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getPassword());       
        User newUser = newUserOptional.get();
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User registered successfully {}", newUser.getEmail());
        return new TokenResponse(jwtService.generateToken(authentication));
    }
      
    @Operation(summary = "Login a user", description = "Logs in a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Successful login",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = TokenResponse.class),
                           examples = @ExampleObject(name = "Successful Response", value = "{\"token\": \"jwt\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid credentials",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = ErrorResponse.class),
                           examples = @ExampleObject(name = "Unauthorized", value = "{\"message\": \"error\"}")))
    @PostMapping("/login")
    public TokenResponse loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("login {}", loginRequest.getLogin());
        
    	Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            loginRequest.getLogin(), 
	            loginRequest.getPassword()
	        )
	    );

        log.info("Login successful for {}", loginRequest.getLogin());
	    return new TokenResponse(jwtService.generateToken(authentication));
    }

    @Operation(summary = "Get user details", description = "Returns user details of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = UserDto.class),
                           examples = @ExampleObject(name = "User Info", value = "{\"id\": 1, \"email\": \"user@example.com\", \"name\": \"John Doe\", \"createdAt\": \"2021-01-01T00:00:00Z\", \"updatedAt\": \"2021-01-01T00:00:00Z\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Bad or Missing Token",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Empty object", value = "{}")))    
    @GetMapping("/me")
    public UserDto getUserInfo(Authentication authentication) {
        log.info("getUserInfo {} - Fetching user info for {}", authentication.getName());
        
        String email = authentication.getName(); 
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;    
    }
}

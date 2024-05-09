package com.chatop.rental.controller;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.dto.requests.LoginRequest;
import com.chatop.rental.dto.requests.RegisterRequest;
import com.chatop.rental.dto.responses.ErrorResponse;
import com.chatop.rental.dto.responses.TokenResponse;
import com.chatop.rental.service.interfaces.AuthService;
import com.chatop.rental.service.interfaces.JwtService;
import com.chatop.rental.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    
    @Autowired
    private AuthService authService;
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
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
        log.info("Registering {}", registerRequest.getEmail());
        return authService.registerAndGenerateToken(registerRequest);
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
        log.info("Logging in {}", loginRequest.getEmail());
        return authService.authenticateAndGenerateToken(loginRequest);
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
    public Optional<UserDto> getUserInfo(Authentication authentication) {
        log.info("Fetching user info for {}", authentication.getName());
        return userService.getUserDetails(authentication.getName());
    }
}

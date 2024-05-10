package com.chatop.rental.controller;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.dto.requests.LoginRequest;
import com.chatop.rental.dto.requests.RegisterRequest;
import com.chatop.rental.dto.responses.ErrorResponse;
import com.chatop.rental.dto.responses.TokenResponse;
import com.chatop.rental.exception.AuthenticationException;
import com.chatop.rental.exception.BadRequestException;
import com.chatop.rental.service.interfaces.AuthService;
import com.chatop.rental.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
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
    private final AuthService authService;
    
    public AuthController(UserService userService,  AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }
    
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Successful registration",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Validation Failed", value = "{}")))    
    @PostMapping("/register")
    public TokenResponse registerUser(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        log.info("Registering {}", registerRequest.getEmail());
        if (bindingResult.hasErrors()) {
            String errorDetails = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
            log.error("Register : Validation failed for user {}: {}", registerRequest.getEmail(), errorDetails);
            throw new BadRequestException();
        }
        return authService.registerAndGenerateToken(registerRequest);
    }
     
    
    @Operation(summary = "Login a user", description = "Logs in a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Successful login",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = TokenResponse.class),
                           examples = @ExampleObject(name = "Successful Response", value = "{\"token\": \"jwt\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = ErrorResponse.class),
                           examples = @ExampleObject(name = "invalid credentials", value = "{\"message\": \"error\"}")))
    @PostMapping("/login")
    public TokenResponse loginUser(@RequestBody @Valid  LoginRequest loginRequest, BindingResult bindingResult) {
        log.info("Logging in {}", loginRequest.getEmail());
        if (bindingResult.hasErrors()) {
            String errorDetails = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
            log.error("Login : Validation failed for user {}: {}", loginRequest.getEmail(), errorDetails);
            throw new AuthenticationException("error");
        }
        return authService.authenticateAndGenerateToken(loginRequest);
    }
    
    @Operation(summary = "Get user details", description = "Returns user details of the currently authenticated user")
    @ApiResponse(responseCode = "200", description = "User details retrieved successfully",
        content = @Content(mediaType = "application/json",
                           schema = @Schema(implementation = UserDto.class),
                           examples = @ExampleObject(name = "User Info", value = "{\"id\": 1, \"email\": \"user@example.com\", \"name\": \"John Doe\", \"createdAt\": \"2021-01-01T00:00:00Z\", \"updatedAt\": \"2021-01-01T00:00:00Z\"}")))
    @ApiResponse(responseCode = "401", description = "Unauthorized",
    content = @Content(mediaType = "application/json",
                       examples = @ExampleObject(name = "Bad or Missing Token", value = "{}")))    
    @GetMapping("/me")
    public Optional<UserDto> getUserInfo(Authentication authentication) {
        log.info("Fetching user info for {}", authentication.getName());
        return userService.getUserDetails(authentication.getName());
    }
}

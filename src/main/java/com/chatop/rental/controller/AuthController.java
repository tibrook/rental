package com.chatop.rental.controller;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;
import com.chatop.rental.service.JWTService;
import com.chatop.rental.service.UserService;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final JWTService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
	
    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        User newUser = userService.registerUser(payload.get("email"), payload.get("name"), payload.get("password"));
        if (newUser == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "error"));
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(Map.of("token", token));
    }
      
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    payload.get("login"), 
                    payload.get("password")
                )
            );

            String token = jwtService.generateToken(authentication);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
        }
    }

    
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
    	System.out.println("GetUserInfo****");

        String email = authentication.getName(); 
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName(), user.getCreatedAt(), user.getUpdatedAt());
        return ResponseEntity.ok(userDto);
    }
}

package com.chatop.rental.controller;

import com.chatop.rental.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        if (userService.registerUser(payload.get("email"), payload.get("name"), payload.get("password")) == null) {
            return ResponseEntity.badRequest().body("{}");
        }
        // To do - Implement JWT token 
        return ResponseEntity.ok("{'token': 'jwt'}");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        if (!userService.authenticateUser(payload.get("email"), payload.get("password"))) { 
            return ResponseEntity.status(401).body("{'message': 'error'}");
        }
        return ResponseEntity.ok("{'token': 'jwt'}");
    }
}

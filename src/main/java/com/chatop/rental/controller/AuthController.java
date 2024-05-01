package com.chatop.rental.controller;

import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        if (userRepository.findByEmail(payload.get("email")) != null) {
            return ResponseEntity.badRequest().body("{}");
        }
        User newUser = new User();
        newUser.setEmail(payload.get("email"));
        newUser.setPassword(payload.get("password")); 
        newUser.setName(payload.get("name"));
        newUser.setCreatedAt(new Date());
        userRepository.save(newUser);
        // To do - Implement JWT token 
        return ResponseEntity.ok("{'token': 'jwt'}");
    }
}

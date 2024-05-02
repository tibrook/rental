package com.chatop.rental.service;

import com.chatop.rental.model.User;

public interface UserService {
    User registerUser(String email, String name, String password);
    boolean authenticateUser(String email, String password);
    User getUserById(Long userId);
    User findByEmail(String email);
}

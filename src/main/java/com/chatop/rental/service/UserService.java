package com.chatop.rental.service;

import java.util.Optional;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;

public interface UserService {
  	Optional<User> registerUser(String email, String name, String password);
    boolean authenticateUser(String email, String password);
    Optional<UserDto> getUserById(Long userId);
    Optional<User> findByEmail(String email);
    Optional<UserDto> getUserDetails(String email);
}

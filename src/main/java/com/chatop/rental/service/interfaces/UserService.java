package com.chatop.rental.service.interfaces;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;

public interface UserService {
  	Optional<User> registerUser(String email, String name, String password);
    boolean authenticateUser(String email, String password);
    UserDto getUserById(Long userId);
    Optional<User> findByEmail(String email);
    Optional<UserDto> getUserDetails(String email);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

package com.chatop.rental.service;

import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(String email, String name, String password) {
        if (userRepository.findByEmail(email) != null) {
            return null;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Encrypt password before storing
        newUser.setName(name);
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }
    
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    @Override
    public User findByEmail(String email) { 
        return userRepository.findByEmail(email);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}

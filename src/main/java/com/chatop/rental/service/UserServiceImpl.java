package com.chatop.rental.service;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public Optional<User> registerUser(String email, String name, String password) {
        log.info("Attempting to register new user with email: {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Registration attempt failed: Email {} already in use.", email);
            return Optional.empty();
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        log.info("New user registered with email: {}", email);
        return Optional.of(userRepository.save(newUser));
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        log.info("Authenticating user with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            log.info("Authentication successful for email: {}", email);
            return true;
        }
        log.info("Authentication failed for email: {}", email);
        return false;
    }

    public Optional<UserDto> getUserDetails(String email) {
        log.info("Fetching user details for email: {}", email);
        return findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
    
    @Override
    public Optional<User> getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);
        Optional<User> user = userRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), Collections.emptyList());
    }
}

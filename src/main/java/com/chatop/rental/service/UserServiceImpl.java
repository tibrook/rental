package com.chatop.rental.service;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;
import com.chatop.rental.service.interfaces.UserService;

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
        return userRepository.findByEmail(email)
                             .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                             .isPresent();
    }

    public Optional<UserDto> getUserDetails(String email) {
        log.info("Fetching user details for email: {}", email);
        return findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
    
    @Override
    public Optional<UserDto> getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
        		.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                             .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList()))
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}

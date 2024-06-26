package com.chatop.rental.service;

import com.chatop.rental.dto.UserDto;
import com.chatop.rental.exception.AuthenticationException;
import com.chatop.rental.exception.BadRequestException;
import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;
import com.chatop.rental.service.interfaces.UserService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
    	this.userRepository = userRepository;
    	this.passwordEncoder=passwordEncoder;
    	this.modelMapper=modelMapper;
    }
    /**
     * Registers a new user.
     * @param email Email of the user to be registered.
     * @param name Name of the user to be registered.
     * @param password Password of the user to be registered.
     * @return Optional containing the registered user if successful, empty otherwise.
     */
    @Override
    public User registerUser(String email, String name, String password) {
        log.info("Attempting to register new user with email: {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            log.error("Registration attempt failed: Email {} already in use.", email);
            throw new BadRequestException();
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        log.info("New user registered with email: {}", email);
        return userRepository.save(newUser);
    }
    /**
     * Authenticates a user.
     * @param email Email of the user to be authenticated.
     * @param password Password of the user to be authenticated.
     * @return true if authentication is successful, false otherwise.
     */
    @Override
    public boolean authenticateUser(String email, String password) {
        log.info("Authenticating user with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.error("Authentication failed: No user found with email {}", email);
            throw new AuthenticationException("error");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            log.error("Authentication failed: Incorrect password for email {}", email);
            throw new AuthenticationException("error");
        }
        return true;
    }
    /**
     * Retrieves details of a user by email.
     * @param email Email of the user.
     * @return Optional containing the user details if found, empty otherwise.
     */
    public Optional<UserDto> getUserDetails(String email) {
        log.info("Fetching user details for email: {}", email);
        return findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));
    }
    
    /**
     * Retrieves details of a user by ID.
     * @param userId ID of the user.
     * @return Optional containing the user details if found, empty otherwise.
     */
    @Override
    public Optional<UserDto> getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
            .map(user -> modelMapper.map(user, UserDto.class));
    }

    /**
     * Retrieves a user by email.
     * @param email Email of the user.
     * @return Optional containing the user if found, empty otherwise.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Searching for user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    /**
     * Loads a user by username (email) for Spring Security.
     * @param username Username (email) of the user.
     * @return UserDetails object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                             .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList()))
                             .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}

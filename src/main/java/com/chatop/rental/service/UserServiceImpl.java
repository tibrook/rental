package com.chatop.rental.service;

import com.chatop.rental.model.User;
import com.chatop.rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(String email, String name, String password) {
        if (userRepository.findByEmail(email) != null) {
            return null;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password); // Note: Password should be encrypted before storing.
        newUser.setName(name);
        newUser.setCreatedAt(new Date());
        return userRepository.save(newUser);
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}

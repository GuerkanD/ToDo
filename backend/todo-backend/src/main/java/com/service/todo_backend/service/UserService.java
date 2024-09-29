package com.service.todo_backend.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.RegisterRequestDTO;
import com.service.todo_backend.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public boolean createUser(RegisterRequestDTO register) {
        String encodedPassword = authService.encodePassword(register.password());
        try {
            userRepository.save(new User(register.firstname(), register.lastname(), register.email(), encodedPassword));
            return true;
        } catch (Exception e) {
            logger.error("Error while registering user: {}", e.getMessage());
            return false;
        }
    }

    public boolean doesEmailExist(String email) { 
        return userRepository.existsByEmail(email);
    }

    public boolean isUserValid(Long id) {
        return userRepository.existsById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}

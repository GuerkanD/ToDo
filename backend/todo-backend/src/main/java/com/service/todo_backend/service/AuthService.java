package com.service.todo_backend.service;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.LoginRequestDTO;
import com.service.todo_backend.payload.in.RegisterRequestDTO;
import com.service.todo_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public boolean createUser(RegisterRequestDTO register) {
        String encodedPassword = encoder.encode(register.password());
        try {
            userRepository.save(new User(register.firstname(), register.lastname(), register.email(), encodedPassword));
            return true;
        } catch (Exception e) {
            logger.error("Error while registering user: {}", e.getMessage());
            return false;
        }
    }

    public boolean comparePasswords(LoginRequestDTO login) {
        try {
            User user = userRepository.findByEmail(login.email());
            return encoder.matches(login.password(), user.getPassword());
        } catch (Exception e) {
            logger.error("Error while comparing password: {}", e.getMessage());
            return false;
        }
    }
}

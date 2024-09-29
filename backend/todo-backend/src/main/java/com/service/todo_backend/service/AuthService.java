package com.service.todo_backend.service;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.LoginRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final BCryptPasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    public boolean comparePasswords(LoginRequestDTO login,User user) {
        try {
            return encoder.matches(login.password(), user.getPassword());
        } catch (Exception e) {
            logger.error("Error while comparing password: {}", e.getMessage());
            return false;
        }
    }
}

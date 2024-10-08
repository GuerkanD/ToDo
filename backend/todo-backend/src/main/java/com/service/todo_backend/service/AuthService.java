package com.service.todo_backend.service;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.LoginRequestDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {
    private final BCryptPasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordSecurityService passwordSecurityService;

    public AuthService(BCryptPasswordEncoder encoder, UserRepository userRepository, PasswordSecurityService passwordSecurityService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.passwordSecurityService = passwordSecurityService;
    }

    public boolean isEmailValid(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    public boolean comparePasswords(LoginRequestDTO login, User user) {
        try {
            return encoder.matches(login.password(), user.getPassword());
        } catch (Exception e) {
            logger.error("Error while comparing password: {}", e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(Long id, String password) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                return false;
            }
            user.get().setPassword(encodePassword(password));
            userRepository.save(user.orElseThrow());
            return true;
        } catch (Exception e) {
            logger.error("Error while Updating Password");
            return false;
        }
    }

    public boolean validatePassword (String password){
        return (passwordSecurityService.checkPasswordSecurity(password)) && (password.length() < 72) && (password.length() > 8);
    }
}

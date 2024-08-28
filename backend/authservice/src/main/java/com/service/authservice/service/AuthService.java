package com.service.authservice.service;

import com.service.authservice.model.User;
import com.service.authservice.payload.in.RegisterRequestDTO;
import com.service.authservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean doesEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean doesUserExistById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean createUser(RegisterRequestDTO registerRequestDTO) {
        try {
            userRepository.save(new User(registerRequestDTO.firstname(), registerRequestDTO.lastname(), registerRequestDTO.email(), registerRequestDTO.password()));
            return true;
        } catch (Exception e) {
            logger.error("Error while registering user: {}", e.getMessage());
            return false;
        }
    }
}

package com.service.todo_backend.controller;

import com.service.todo_backend.model.User;
import com.service.todo_backend.payload.in.LoginRequestDTO;
import com.service.todo_backend.payload.in.RegisterRequestDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.AuthService;
import com.service.todo_backend.service.JwtService;
import com.service.todo_backend.service.PasswordSecurityService;
import com.service.todo_backend.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final PasswordSecurityService passwordSecurityService;
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(PasswordSecurityService passwordSecurityService, AuthService authService, JwtService jwtService, UserService userService) {
        this.passwordSecurityService = passwordSecurityService;
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService; 
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        if (registerRequest.password().length() < 8) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Password must be at least 8 characters long!"));
        }
        if (registerRequest.password().length() > 72) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Password must be at most 72 characters long!"));
        }
        if (!passwordSecurityService.checkPasswordSecurity(registerRequest.password())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character!"));
        }
        if (!authService.isEmailValid(registerRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Invalid email format!"));
        }
        if (userService.doesEmailExist(registerRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email is already in use!"));
        }
        if (userService.createUser(registerRequest)) {
            return ResponseEntity.ok(new MessageResponseDTO("User registered successfully!"));
        }
        return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error: User registration failed!"));
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Optional<User> user = userService.getUserByEmail(loginRequest.email());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email or Password is wrong!"));
        }
        if (!authService.comparePasswords(loginRequest,user.get())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email or Password is wrong!"));
        }
        String generateToken = jwtService.generateToken(user.get());
        return ResponseEntity.ok(new MessageResponseDTO(generateToken));
    }
}

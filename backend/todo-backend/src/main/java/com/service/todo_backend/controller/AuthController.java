package com.service.todo_backend.controller;

import com.service.todo_backend.payload.in.LoginRequestDTO;
import com.service.todo_backend.payload.in.RegisterRequestDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.service.AuthService;
import com.service.todo_backend.service.PasswordSecurityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    private final PasswordSecurityService passwordSecurityService;
    private final AuthService authService;

    public AuthController(PasswordSecurityService passwordSecurityService, AuthService authService) {
        this.passwordSecurityService = passwordSecurityService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        if (registerRequest.password().length() < 8) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Password must be at least 8 characters long!"));
        }
        if (passwordSecurityService.checkPasswordSecurity(registerRequest.password())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character!"));
        }
        if (!authService.isEmailValid(registerRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Invalid email format!"));
        }
        if (authService.doesEmailExist(registerRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email is already in use!"));
        }
        if (authService.createUser(registerRequest)) {
            return ResponseEntity.ok(new MessageResponseDTO("User registered successfully!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: User registration failed!"));
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(new MessageResponseDTO("User logged in successfully!"));
    }
}

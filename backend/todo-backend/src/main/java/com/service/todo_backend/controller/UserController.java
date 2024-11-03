package com.service.todo_backend.controller;

import com.service.todo_backend.payload.in.UserUpdateDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.payload.out.UserResponseDTO;
import com.service.todo_backend.service.AuthService;
import com.service.todo_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping()
    public ResponseEntity<UserResponseDTO> getUserDetails(Authentication authentication) {
        UserResponseDTO user = userService.getUserDetails((Long) authentication.getPrincipal());
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @PutMapping()
    public ResponseEntity<MessageResponseDTO> updateUserDetails(Authentication authentication, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        if (!authService.isEmailValid(userUpdateDTO.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Invalid email format!"));
        }
        if (userService.doesEmailExist(userUpdateDTO.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email is already in use!"));
        }
        HttpStatus status = userService.updateUser(userUpdateDTO, (Long) authentication.getPrincipal());
        if (status == HttpStatus.OK)
            return ResponseEntity.ok(new MessageResponseDTO("Successfully Updated User Details"));
        if (status == HttpStatus.NOT_FOUND)
            return ResponseEntity.status(status).body(new MessageResponseDTO("Error: The User to be updated could not be found"));
        return ResponseEntity.status(status).body(new MessageResponseDTO("Error: Something went wrong while Updating the User"));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponseDTO> deleteUser(Authentication authentication) {
        HttpStatus status = userService.deleteUser((Long) authentication.getPrincipal());
        if (status == HttpStatus.OK)
            return ResponseEntity.ok(new MessageResponseDTO("Successfully Deleted Account"));
        if (status == HttpStatus.NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDTO("Error: The User to delete does not exist"));
        return ResponseEntity.status(status).body(new MessageResponseDTO("Error: Something went wrong while deleting the User"));
    }
}

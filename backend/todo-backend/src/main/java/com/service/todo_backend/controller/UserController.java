package com.service.todo_backend.controller;

import com.service.todo_backend.payload.in.UserUpdateDTO;
import com.service.todo_backend.payload.out.MessageResponseDTO;
import com.service.todo_backend.payload.out.UserResponseDTO;
import com.service.todo_backend.service.AuthService;
import com.service.todo_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController (UserService userService, AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping()
    public ResponseEntity<UserResponseDTO> getUserDetails(Authentication authentication){
        UserResponseDTO user = userService.getUserDetails((Long)authentication.getPrincipal());
        return  user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }

    @PutMapping()
    public ResponseEntity<MessageResponseDTO> updateUserDetails(Authentication authentication, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        if (!authService.isEmailValid(userUpdateDTO.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Invalid email format!"));
        }
        if (userService.doesEmailExist(userUpdateDTO.email())) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error: Email is already in use!"));
        }
        if (userService.updateUser(userUpdateDTO,(Long)authentication.getPrincipal())){
            return ResponseEntity.ok(new MessageResponseDTO("Successfully Updated User Details"));
        }
        return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error: Something went wrong while Updating the User"));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponseDTO> deleteUser(Authentication authentication) {
        if (userService.deleteUser((Long) authentication.getPrincipal())){
            return ResponseEntity.ok(new MessageResponseDTO("Successfully Deleted Account"));
        }
        return ResponseEntity.internalServerError().body(new MessageResponseDTO("Error: Something went wrong while Deleting the User"));
    }
}

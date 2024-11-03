package com.service.todo_backend.service;

import java.util.Optional;

import com.service.todo_backend.payload.in.UserUpdateDTO;
import com.service.todo_backend.payload.out.UserResponseDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    public UserResponseDTO getUserDetails(Long userId) {
        try {
            Optional<User> user = userRepository.findById(userId);
            return user.map(u -> new UserResponseDTO(
                    u.getEmail(),
                    u.getFirstname(),
                    u.getLastname(),
                    u.getCreatedAt()
            )).orElse(null);
        } catch (Exception e) {
            logger.error("Error while fetching User Data {}", e.getMessage());
            return null;
        }
    }

    public HttpStatus updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                user.map(u -> {
                    u.setEmail(userUpdateDTO.email());
                    u.setFirstname(userUpdateDTO.firstname());
                    u.setLastname(userUpdateDTO.lastname());
                    return u;
                });
                userRepository.save(user.get());
                return HttpStatus.OK;
            } else {
                logger.error("Error: User not found");
                return HttpStatus.NOT_FOUND;
            }
        } catch (Exception e) {
            logger.error("Error while Updating user: {}", e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Transactional
    public HttpStatus deleteUser(Long id){
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                return HttpStatus.NOT_FOUND;
            }
            userRepository.delete(user.get());
            return HttpStatus.OK;
        } catch (Exception e){
            logger.error("Error while Updating the User {}",e.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}

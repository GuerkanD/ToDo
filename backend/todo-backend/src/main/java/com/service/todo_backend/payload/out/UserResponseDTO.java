package com.service.todo_backend.payload.out;

import java.time.LocalDateTime;

public record UserResponseDTO(String email, String firstname, String lastname, LocalDateTime creationDate) {
}

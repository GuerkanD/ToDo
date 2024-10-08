package com.service.todo_backend.payload.out;


import java.time.LocalDateTime;

public record TaskResponseDTO(Long id, String title, String description, boolean status, LocalDateTime creationDate) {
}

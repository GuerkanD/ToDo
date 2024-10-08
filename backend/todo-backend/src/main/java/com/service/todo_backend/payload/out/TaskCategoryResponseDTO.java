package com.service.todo_backend.payload.out;

import java.time.LocalDateTime;

public record TaskCategoryResponseDTO(Long id, Long categoryId, String title, String description, boolean status, LocalDateTime creationDate) {
}

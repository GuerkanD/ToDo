package com.service.todo_backend.payload.in;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String email, @NotBlank String password) {
}

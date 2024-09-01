package com.service.todo_backend.payload.in;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String firstname,@NotBlank String lastname, @NotBlank String email, @NotBlank String password) {
}

package com.service.authservice.payload.in;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(@NotBlank String username, @NotBlank String email, @NotBlank String password) {
}

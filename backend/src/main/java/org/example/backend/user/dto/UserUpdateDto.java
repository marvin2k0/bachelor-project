package org.example.backend.user.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDto(
        String username,

        @Email
        String email
) {
}

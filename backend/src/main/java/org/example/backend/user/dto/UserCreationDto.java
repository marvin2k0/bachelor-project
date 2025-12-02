package org.example.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreationDto(
        @NotBlank
        @Size(min = 3, message = "At least three characters needed for username")
        String username,

        @NotBlank
        @Email(message = "Email format invalid")
        String email
) {
}

package org.example.backend.user.dto;

public record UserDto(
        long id,
        String username,
        String matriculationNumber,
        String email
) {
}

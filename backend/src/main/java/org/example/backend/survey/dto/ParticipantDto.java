package org.example.backend.survey.dto;

public record ParticipantDto(
        Long id,
        String username,
        String matriculationNumber,
        String email
) {
}

package org.example.backend.survey.dto;

public record ParticipantDto(
        Long id,
        String name,
        String matriculationNumber,
        String email
) {
}

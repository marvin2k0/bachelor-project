package org.example.backend.survey.dto;

public record StudentCsvRowDto(
        String matriculationNumber,
        String name,
        String email,
        int lineNumber
) {
}

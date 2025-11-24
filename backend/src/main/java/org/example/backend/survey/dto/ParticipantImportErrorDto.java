package org.example.backend.survey.dto;

public record ParticipantImportErrorDto(
        int lineNumber,
        String message,
        String rawLine
) {
}

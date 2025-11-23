package org.example.backend.group.dto;

public record GroupCreationDto(
        String name,
        int surveyId,
        int capacity
) {
}

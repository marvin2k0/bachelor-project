package org.example.backend.survey.dto;

import java.time.LocalDateTime;

public record SurveyCreationDto(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int groupCount
) {
}

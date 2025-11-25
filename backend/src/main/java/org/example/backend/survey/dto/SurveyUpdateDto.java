package org.example.backend.survey.dto;

import java.time.LocalDateTime;

public record SurveyUpdateDto(
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}

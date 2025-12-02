package org.example.backend.survey.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SurveyCreationDto(
        @NotBlank(message = "Survey name cannot be empty")
        @Size(min = 2, message = "At least three characters needed for survey name")
        String name,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        @Min(value = 1, message = "At least one group needed")
        int groupCount
) {
}

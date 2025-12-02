package org.example.backend.group.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupCreationDto(
        @NotBlank
        @Size(min = 1, message = "At least one characters needed for group name")
        String name,

        @NotBlank
        long surveyId,

        @NotBlank
        @Min(1)
        int capacity
) {
}

package org.example.backend.survey.dto;

import org.example.backend.user.dto.UserDto;

import java.time.LocalDateTime;

public record SurveyDto(
        long id,
        String name,
        String description,
        UserDto creator,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int groupCount,
        int participantCount
) {
}

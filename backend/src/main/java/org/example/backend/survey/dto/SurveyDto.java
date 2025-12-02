package org.example.backend.survey.dto;

import org.example.backend.group.dto.GroupDto;
import org.example.backend.user.User;
import org.example.backend.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

public record SurveyDto(
        String name,
        String description,
        UserDto creator,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<UserDto> participants,
        List<GroupDto> groups
) {
}

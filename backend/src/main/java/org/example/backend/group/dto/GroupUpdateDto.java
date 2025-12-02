package org.example.backend.group.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.example.backend.user.dto.UserDto;

import java.util.List;

public record GroupUpdateDto(
        String name,
        @Min(1)
        int capacity,
        List<UserDto> members
) {
}

package org.example.backend.user.dto;

import org.example.backend.group.dto.GroupDto;

import java.util.ArrayList;
import java.util.List;

public record UserDto(
        long id,
        String username,
        String matriculationNumber,
        String email,
        List<GroupDto> groups
) {
    public UserDto {
        if (groups == null) {
            groups = new ArrayList<>();
        }
    }
}

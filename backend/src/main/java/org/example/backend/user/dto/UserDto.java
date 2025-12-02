package org.example.backend.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.backend.group.dto.GroupDto;

import java.util.ArrayList;
import java.util.List;

public record UserDto(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email forma is invalid")
        String email,

        @Valid
        List<GroupDto> groups
) {
    public UserDto {
        if (groups == null) {
            groups = new ArrayList<>();
        }
    }
}

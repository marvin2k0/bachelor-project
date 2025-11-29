package org.example.backend.groupPreference.dto;

public record GroupPreferenceDto(
        long user,
        long group,
        int priority
) {
}

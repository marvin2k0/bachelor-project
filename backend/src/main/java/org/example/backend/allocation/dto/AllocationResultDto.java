package org.example.backend.allocation.dto;

import java.util.List;
import java.util.Map;

public record AllocationResultDto(
        Map<String, List<Map<String, Object>>> groups,
        List<Map<String, Object>> unassignedStudents
) {
}

package org.example.backend.allocation.dto;

import org.example.backend.user.User;

import java.util.List;
import java.util.Map;

public record AllocationResult(
        Map<Long, List<User>> groupAssignments,
        List<User> unassignedStudents
) {
}

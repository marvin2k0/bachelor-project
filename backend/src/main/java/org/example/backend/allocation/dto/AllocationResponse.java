package org.example.backend.allocation.dto;

public record AllocationResponse(
        String message,
        String score,
        long assignedCount,
        long unassignedCount
) {
}

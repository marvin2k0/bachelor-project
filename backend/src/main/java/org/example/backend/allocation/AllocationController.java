package org.example.backend.allocation;

import lombok.RequiredArgsConstructor;
import org.example.backend.allocation.dto.AllocationResponse;
import org.example.backend.allocation.dto.AllocationResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/allocations")
@RequiredArgsConstructor
public class AllocationController {
    private final AllocationService allocationService;

    @PostMapping("/{surveyId}/allocate")
    public ResponseEntity<AllocationResponse> allocateGroups(@PathVariable long surveyId) {
        try {
            AllocationSolution solution = allocationService.allocateSurvey(surveyId);

            return ResponseEntity.ok(new AllocationResponse(
                    "Allocation completed successfully",
                    solution.getScore().toString(),
                    solution.getAssignments().stream()
                            .filter(a -> a.getAssignedGroup() != null)
                            .count(),
                    solution.getAssignments().stream()
                            .filter(a -> a.getAssignedGroup() == null)
                            .count()
            ));
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError()
                    .body(new AllocationResponse("Allocation failed: " + e.getMessage(), null, 0, 0));
        }
    }

    @GetMapping("/{surveyId}/result")
    public ResponseEntity<AllocationResultDto> getResult(@PathVariable long surveyId, @RequestParam(required = false, defaultValue = "username,matriculationNumber") List<String> fields) {
        final AllocationResultDto result = allocationService.getResult(surveyId, fields);
        return ResponseEntity.ok(result);
    }
}

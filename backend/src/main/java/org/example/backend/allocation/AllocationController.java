package org.example.backend.allocation;

import lombok.RequiredArgsConstructor;
import org.example.backend.allocation.dto.AllocationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/allocation")
@RequiredArgsConstructor
public class AllocationController {

    @PostMapping("/surveys/{surveyId}/allocate")
    public ResponseEntity<AllocationResponse> allocateGroups(@PathVariable long surveyId) {
        return null;
    }

    @GetMapping("/surveys/{surveyId}/result")
    public void getResult(@PathVariable long surveyId) {

    }
}

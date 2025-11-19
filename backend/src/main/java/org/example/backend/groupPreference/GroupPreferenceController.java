package org.example.backend.groupPreference;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/grouppreference")
@RequiredArgsConstructor
public class GroupPreferenceController {
    private final GroupPreferenceService service;

    @GetMapping("/")
    public ResponseEntity<List<GroupPreference>> getAllGroupPreferences() {
        return ResponseEntity.ok(service.getAllGroupPreferences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupPreference> getGroupPreferenceDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGroupPreferenceById(id));
    }

    @PostMapping("/")
    public ResponseEntity<GroupPreference> addGroupPreference(@RequestBody GroupPreference groupPreference) {
        return ResponseEntity.ok(service.saveGroupPreference(groupPreference));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupPreference> updateGroupPreference(@PathVariable Long id, @RequestBody GroupPreference groupPreference) {
        return ResponseEntity.ok(service.updateGroupPreference(id, groupPreference));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupPreference(@PathVariable Long id) {
        service.deleteGroupPreferenceById(id);
        return ResponseEntity.ok().build();
    }
}
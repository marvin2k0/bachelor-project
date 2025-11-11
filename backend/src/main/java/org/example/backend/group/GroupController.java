package org.example.backend.group;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;

    @GetMapping("/")
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(service.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGroupById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Group> addGroup(@RequestBody Group group) {
        return ResponseEntity.ok(service.saveGroup(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group group) {
        return ResponseEntity.ok(service.updateGroup(id, group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }
}
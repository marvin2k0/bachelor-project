package org.example.backend.group;

import lombok.RequiredArgsConstructor;

import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
public class GroupController {
    private final SurveyService surveyService;
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
    public ResponseEntity<Group> addGroup(@RequestBody GroupCreationDto groupDto) {
        final Survey survey = surveyService.getSurveyById(groupDto.surveyId());

        if (survey == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        final Group group = Group.builder()
                .name(groupDto.name())
                .capacity(groupDto.capacity())
                .survey(survey)
                .build();

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
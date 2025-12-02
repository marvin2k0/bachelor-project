package org.example.backend.group;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.group.dto.GroupDto;
import org.example.backend.group.dto.GroupUpdateDto;
import org.example.backend.mappers.GroupMapper;
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
    private final GroupMapper mapper;

    @GetMapping("/")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        return ResponseEntity.ok(this.mapper.toDto(service.getAllGroups()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getGroupDetails(@PathVariable Long id) {
        return ResponseEntity.ok(this.mapper.toDto(service.getGroupById(id)));
    }

    @PostMapping("/")
    public ResponseEntity<GroupDto> addGroup(@Valid @RequestBody GroupCreationDto groupCreationDto) {
        final Survey survey = surveyService.getSurveyById(groupCreationDto.surveyId());

        if (survey == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(this.mapper.toDto(service.saveGroup(groupCreationDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupUpdateDto group) {
        return ResponseEntity.ok(this.mapper.toDto(service.updateGroup(id, group)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }
}
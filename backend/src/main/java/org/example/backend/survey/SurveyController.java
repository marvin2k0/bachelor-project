package org.example.backend.survey;

import lombok.RequiredArgsConstructor;

import org.example.backend.group.Group;
import org.example.backend.group.GroupService;
import org.example.backend.survey.dto.ParticipantImportResultDto;
import org.example.backend.survey.dto.SurveyCreationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService service;
    private final GroupService groupService;

    @GetMapping("/")
    public ResponseEntity<List<Survey>> getAllSurveys() {
        return ResponseEntity.ok(service.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSurveyById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Survey> addSurvey(@RequestBody SurveyCreationDto surveyDto) {

        final Survey survey = Survey.builder()
                .name(surveyDto.name())
                .startTime(surveyDto.startTime())
                .endTime(surveyDto.endTime())
                .build();

        service.saveSurvey(survey);

        for (int i = 0; i < surveyDto.groupCount(); i++) {
            final Group group = Group.builder()
                    .name("T" + (i<10 ? "0" : "") + i+1)
                    .capacity(6)
                    .survey(survey)
                    .build();

            groupService.saveGroup(group);
        }

        return ResponseEntity.ok(survey);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody Survey survey) {
        return ResponseEntity.ok(service.updateSurvey(id, survey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        service.deleteSurveyById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{surveyId}/participants/import")
    public ResponseEntity<ParticipantImportResultDto> importParticipants(
            @PathVariable Long surveyId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(service.importParticipantsFromCsv(surveyId, file));
    }
}
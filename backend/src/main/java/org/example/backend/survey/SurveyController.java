package org.example.backend.survey;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.backend.group.GroupService;
import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.mappers.SurveyMapper;
import org.example.backend.survey.dto.ParticipantImportResultDto;
import org.example.backend.survey.dto.SurveyCreationDto;
import org.example.backend.survey.dto.SurveyDto;
import org.example.backend.survey.dto.SurveyUpdateDto;
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
    private final SurveyMapper mapper;

    @GetMapping("/")
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(this.mapper.toDto(service.getAllSurveys()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(this.mapper.toDto(service.getSurveyById(id)));
    }

    @PostMapping("/")
    public ResponseEntity<SurveyDto> addSurvey(@Valid @RequestBody SurveyCreationDto surveyCreationDto) {
        final Survey survey = service.saveSurvey(surveyCreationDto);

        for (int i = 0; i < surveyCreationDto.groupCount(); i++) {
            final GroupCreationDto group = new GroupCreationDto(
                    "T" + (i<9 ? "0" : "") + (i+1),
                    survey.getId(),
                    6
            );
            groupService.saveGroup(group);
        }

        return ResponseEntity.ok(this.mapper.toDto(survey));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDto> updateSurvey(@PathVariable Long id, @Valid @RequestBody SurveyUpdateDto surveyUpdateDto) {
        System.out.println(surveyUpdateDto);
        final Survey survey = service.updateSurvey(id, surveyUpdateDto);

        return ResponseEntity.ok(this.mapper.toDto(survey));
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
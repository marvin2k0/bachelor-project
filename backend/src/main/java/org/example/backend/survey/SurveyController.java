package org.example.backend.survey;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.backend.group.GroupService;
import org.example.backend.group.dto.GroupCreationDto;
import org.example.backend.mappers.SurveyMapper;
import org.example.backend.mappers.UserMapper;
import org.example.backend.survey.dto.*;
import org.example.backend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SurveyController {
    private final SurveyService service;
    private final GroupService groupService;
    private final SurveyMapper surveyMapper;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<SurveyDto>> getAllSurveys(@PageableDefault(sort = "id") Pageable pageable) {
        final Page<Survey> page = service.getAllSurveys(pageable);
        return ResponseEntity.ok(page.map(this.surveyMapper::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDto> getSurveyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(this.surveyMapper.toDto(service.getSurveyById(id)));
    }

    @PostMapping
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

        return ResponseEntity.ok(this.surveyMapper.toDto(survey));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurveyDto> updateSurvey(@PathVariable Long id, @Valid @RequestBody SurveyUpdateDto surveyUpdateDto) {
        final Survey survey = service.updateSurvey(id, surveyUpdateDto);
        return ResponseEntity.ok(this.surveyMapper.toDto(survey));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        service.deleteSurveyById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{surveyId}/participants")
    public ResponseEntity<Page<ParticipantDto>> getParticipants(@PathVariable Long surveyId, @PageableDefault(sort = "id") Pageable pageable) {
        final Page<User> page = service.getParticipantsBySurveyId(surveyId, pageable);
        return ResponseEntity.ok(page.map(this.userMapper::toParticipantDto));
    }

    @PostMapping("/{surveyId}/participants/import")
    public ResponseEntity<ParticipantImportResultDto> importParticipants(
            @PathVariable Long surveyId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(service.importParticipantsFromCsv(surveyId, file));
    }
}
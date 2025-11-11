package org.example.backend.survey;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/survey")
@RequiredArgsConstructor
public class SurveyController {
    private final SurveyService service;

    @GetMapping("/")
    public ResponseEntity<List<Survey>> getAllSurveys() {
        return ResponseEntity.ok(service.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyDetails(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSurveyById(id));
    }

    @PostMapping("/")
    public ResponseEntity<Survey> addSurvey(@RequestBody Survey survey) {
        return ResponseEntity.ok(service.saveSurvey(survey));
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
}
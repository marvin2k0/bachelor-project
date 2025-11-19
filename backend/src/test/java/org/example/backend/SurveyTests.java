package org.example.backend;

import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SurveyTests {
    @Autowired
    private SurveyService service;

    private Survey survey;

    @AfterEach
    void cleanup() {
        if (survey != null && survey.getId() != null)
            this.service.deleteSurveyById(survey.getId());

        assertThrows(RuntimeException.class, () -> this.service.getSurveyById(this.survey.getId()));
        this.survey = null;
    }

    @Test
    void test_create_survey() {
        final String surveyName = "SEP";
        this.survey = Survey.builder()
                .name(surveyName)
                .build();
        service.saveSurvey(survey);

        assertTrue(service.getAllSurveys()
                .stream()
                .anyMatch(s -> s.getName().equals(surveyName)));
    }

    @Test
    void test_find_survey_by_name() {
        final String surveyName = "MCI";
        this.survey = Survey.builder()
                .name(surveyName)
                .build();
        service.saveSurvey(survey);

        assertNotNull(service.getSurveyByName(surveyName));
    }

    @Test
    void test_survey_dates() {
        final LocalDateTime now = LocalDateTime.now();
        this.survey = Survey.builder()
                .startTime(now)
                .endTime(now.plusDays(1))
                .build();

        assertTrue(this.survey.isActive(now));
        assertTrue(this.survey.isActive(now.plusHours(12)));
        assertTrue(this.survey.isActive(now.plusHours(24)));
        assertFalse(this.survey.isActive(now.plusHours(25)));
    }
}

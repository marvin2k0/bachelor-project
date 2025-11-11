package org.example.backend;

import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SurveyTests {
    @Autowired
    private SurveyService service;

    @Test
    void survey_create_test() {
        final Survey survey = Survey.builder()
                .name("SEP")
                .build();
        service.saveSurvey(survey);

        assertTrue(service.getAllSurveys()
                .stream()
                .anyMatch(s -> s.getName().equals("SEP")));
    }

    @Test
    void survey_find_by_name() {
        final Survey survey = Survey.builder()
                .name("MCI")
                .build();
        service.saveSurvey(survey);

        assertNotNull(service.getSurveyByName("MCI"));
    }
}

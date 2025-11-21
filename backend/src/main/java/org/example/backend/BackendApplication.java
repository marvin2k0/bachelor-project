package org.example.backend;

import org.example.backend.survey.Survey;
import org.example.backend.survey.SurveyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*
    @Bean
    CommandLineRunner init(SurveyService surveyService) {
        return args -> {
            surveyService.saveSurvey(Survey.builder()
                    .name("SEP")
                    .startTime(LocalDateTime.now())
                    .endTime(LocalDateTime.now().plusWeeks(1))
                    .build());

            surveyService.saveSurvey(Survey.builder()
                    .name("MCI")
                    .endTime(LocalDateTime.now().plusWeeks(1))
                    .build());
            surveyService.saveSurvey(Survey.builder()
                    .name("REM")
                    .startTime(LocalDateTime.now())
                    .build());
        };
    }
     */

}

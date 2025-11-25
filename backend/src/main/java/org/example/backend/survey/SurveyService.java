package org.example.backend.survey;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.backend.group.Group;
import org.example.backend.survey.dto.*;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository repository;
    private final UserRepository userRepository;

    public List<Survey> getAllSurveys() {
        return repository.findAll();
    }

    public Survey saveSurvey(Survey entity) {
        return this.repository.save(entity);
    }

    public Survey getSurveyById(long id) {
        final Optional<Survey> optional = repository.findById(id);
        Survey entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for id:" + id);
        }
        return entity;
    }

    public Survey getSurveyByName(String name) {
        final Optional<Survey> optional = repository.findByName(name);
        Survey entity;
        if (optional.isPresent()) {
            entity = optional.get();
        } else {
            throw new RuntimeException("entity not found for name:" + name);
        }
        return entity;
    }

    public void deleteSurveyById(long id) {
        this.repository.deleteById(id);
    }

    public Survey updateSurvey(Long id, SurveyUpdateDto entity) {
        final Survey survey = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("Survey not found for this id :: " + id));

        survey.setName(entity.name());
        survey.setDescription(entity.description());
        survey.setStartTime(entity.startTime());
        survey.setEndTime(entity.endTime());

        return this.repository.save(survey);
    }

    public ParticipantImportResultDto importParticipantsFromCsv(Long surveyId, MultipartFile file) {
        Survey survey = repository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found: " + surveyId));

        List<ParticipantDto> imported = new ArrayList<>();
        List<ParticipantImportErrorDto> errors = new ArrayList<>();

        int totalRows = 0;
        int successCount = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser parser = CSVFormat.Builder.create()
                    .setDelimiter(',')
                    .setHeader().setSkipHeaderRecord(true)
                    .setIgnoreSurroundingSpaces(true)
                    .build()
                    .parse(reader);

            for (CSVRecord record : parser) {
                totalRows++;
                int lineNumber = (int) record.getRecordNumber() + 1;

                try {
                    StudentCsvRowDto rowDto = mapRecordToDto(record, lineNumber);

                    User user = findOrCreateUser(rowDto);

                    if (!survey.getParticipants().contains(user)) {
                        survey.getParticipants().add(user);
                    }

                    ParticipantDto participantDto = new ParticipantDto(
                            user.getId(),
                            user.getName(),
                            user.getMatriculationNumber(),
                            user.getEmail()
                    );
                    imported.add(participantDto);
                    successCount++;

                } catch (Exception e) {
                    errors.add(new ParticipantImportErrorDto(
                            lineNumber,
                            e.getMessage(),
                            record.toString()
                    ));
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Fehler beim Lesen der CSV-Datei", ex);
        }

        repository.save(survey);

        return new ParticipantImportResultDto(
                surveyId,
                totalRows,
                successCount,
                errors.size(),
                imported,
                errors
        );
    }

    private StudentCsvRowDto mapRecordToDto(CSVRecord record, int lineNumber) {
        String matriculationNumber = record.get("Matrikelnummer").trim();
        String vorname = record.get("Vorname").trim();
        String nachname = record.get("Nachname").trim();
        String name = (vorname + " " + nachname).trim();
        String email = record.get("E-Mail-Adresse").trim();

        if (matriculationNumber.isEmpty()) {
            throw new IllegalArgumentException("Matrikelnummer fehlt");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name fehlt");
        }

        return new StudentCsvRowDto(
                matriculationNumber,
                name,
                email,
                lineNumber
        );
    }

    private User findOrCreateUser(StudentCsvRowDto row) {
        return userRepository.findByMatriculationNumber(row.matriculationNumber())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(row.name())
                            .email(row.email())
                            .matriculationNumber(row.matriculationNumber())
                            .password(null)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
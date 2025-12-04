package org.example.backend.survey;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.backend.mappers.SurveyMapper;
import org.example.backend.survey.dto.*;
import org.example.backend.user.User;
import org.example.backend.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SurveyMapper mapper;

    public Page<User> getParticipantsBySurveyId(long surveyId, Pageable pageable) {
        return this.repository.findParticipantsBySurveyId(surveyId, pageable);
    }

    public Page<Survey> getAllSurveys(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Survey saveSurvey(SurveyCreationDto surveyCreationDto) {
        return this.repository.save(this.mapper.toEntity(surveyCreationDto));
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
        this.mapper.updateEntity(id, entity, survey);

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
                            user.getUsername(),
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
        final String vorname = record.get("Vorname").trim();
        final String nachname = record.get("Nachname").trim();
        final String matriculationNumber = record.get("Matrikelnummer").trim();
        final String name = (vorname + " " + nachname).trim();
        final String email = record.get("E-Mail-Adresse").trim();

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
                            .username(row.name())
                            .email(row.email())
                            .matriculationNumber(row.matriculationNumber())
                            .password(null)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
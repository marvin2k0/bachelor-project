package org.example.backend.survey;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository repository;

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

    public void deleteSurveyById(long id) {
        this.repository.deleteById(id);
    }

    public Survey updateSurvey(Long id, Survey entity) {

        final Survey entityFromDb = repository.findById(id)
                .orElseThrow(() -> new UnsupportedOperationException("Survey not found for this id :: " + id));

        entityFromDb.setId(id);
        this.repository.save(entityFromDb);
        return entityFromDb;
    }
}
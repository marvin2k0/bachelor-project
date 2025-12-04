package org.example.backend.survey;

import org.example.backend.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findByName(String name);

    @Query("SELECT s.participants FROM Survey s WHERE s.id = :surveyId")
    Page<User> findParticipantsBySurveyId(@Param("surveyId") Long surveyId, Pageable pageable);
}
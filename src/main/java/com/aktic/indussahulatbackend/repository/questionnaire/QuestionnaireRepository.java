package com.aktic.indussahulatbackend.repository.questionnaire;

import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire,Long> {
    boolean existsByTitle(String emergencyResponseQuestionnaire);

    Optional<Questionnaire> findByTitle(String emergencyResponseQuestionnaire);
}

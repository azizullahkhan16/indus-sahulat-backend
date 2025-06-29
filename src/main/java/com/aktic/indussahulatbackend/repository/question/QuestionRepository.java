package com.aktic.indussahulatbackend.repository.question;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    Optional<List<Question>> findByQuestionnaireId(Long questionnaireId);

    boolean existsByQuestionnaireId(Long id);
}
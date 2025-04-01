package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Option;
import com.aktic.indussahulatbackend.model.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionDTO {
    private Long id;
    private String question;
    private QuestionnaireDTO questionnaire;
    private List<Option> options;
    private Instant createdAt;
    private Instant updatedAt;

    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.questionnaire = new QuestionnaireDTO(question.getQuestionnaire());
        this.options = question.getOptions();
        this.createdAt = question.getCreatedAt();
        this.updatedAt = question.getUpdatedAt();
    }

}

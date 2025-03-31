package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionnaireDTO {
    private Long id;
    private String title;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public QuestionnaireDTO(Questionnaire questionnaire) {
        this.id = questionnaire.getId();
        this.title = questionnaire.getTitle();
        this.description = questionnaire.getDescription();
        this.createdAt = questionnaire.getCreatedAt();
        this.updatedAt = questionnaire.getUpdatedAt();

    }
}

package com.aktic.indussahulatbackend.service.questionnaire;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Questionnaire;

import java.util.List;

public interface QuestionnaireService {
    List<Question> form(long id);
}
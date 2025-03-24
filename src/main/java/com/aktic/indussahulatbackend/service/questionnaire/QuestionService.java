package com.aktic.indussahulatbackend.service.questionnaire;

import com.aktic.indussahulatbackend.model.response.QuestionForm;

import java.util.List;

public interface QuestionService {

    List<QuestionForm> getAllQuestions();
}
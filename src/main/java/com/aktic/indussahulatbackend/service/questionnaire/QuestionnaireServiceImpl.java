package com.aktic.indussahulatbackend.service.questionnaire;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import com.aktic.indussahulatbackend.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionnaireServiceImpl implements  QuestionnaireService{
    private final QuestionRepository questionRepository;
    public QuestionnaireServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> form(long id) {
        return questionRepository.findAll();
    }
}
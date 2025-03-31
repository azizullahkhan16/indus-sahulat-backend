package com.aktic.indussahulatbackend.service.questionnaire;

import com.aktic.indussahulatbackend.model.entity.Option;
import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Questionnaire;
import com.aktic.indussahulatbackend.model.response.QuestionForm;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.questionnaire.QuestionnaireRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionnaireRepository questionnaireRepository;

    @Override
    public List<QuestionForm> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();

        return questions.stream().map(question -> {
            QuestionForm response = new QuestionForm();
            response.setQuestionId(question.getId());
            response.setQuestion(question.getQuestion());
            response.setOptions(question.getOptions().stream()
                    .map(Option::getOptionText)
                    .collect(Collectors.toList()));
            return response;
        }).collect(Collectors.toList());
    }

}
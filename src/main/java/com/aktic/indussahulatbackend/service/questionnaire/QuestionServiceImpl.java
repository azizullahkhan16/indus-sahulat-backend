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
        saveStaticQuestions();
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

    @Transactional
    public void saveStaticQuestions() {
        if (questionRepository.count() > 0) {
            return;
        }

        Questionnaire questionnaire = getOrCreateQuestionnaire();

        Instant now = Instant.now();

        // Question 1
        List<Option> options1 = List.of(
                Option.builder().optionText("Diabetes").build(),
                Option.builder().optionText("Heart Disease").build(),
                Option.builder().optionText("Cancer").build(),
                Option.builder().optionText("Allergies").build(),
                Option.builder().optionText("Other").build()
        );
        Question q1 = Question.builder()
                .question("Does The Patient Have Any Known Medical Conditions?")
                .questionnaire(questionnaire)
                .createdAt(now)
                .updatedAt(now)
                .options(options1)
                .build();

        // Question 2
        List<Option> options2 = List.of(
                Option.builder().optionText("Yes").build(),
                Option.builder().optionText("No").build()
        );
        Question q2 = Question.builder()
                .question("Is The Patient Conscious And Breathing?")
                .questionnaire(questionnaire)
                .createdAt(now)
                .updatedAt(now)
                .options(new ArrayList<>(options2))
                .build();

        // Question 3
        List<Option> options3 = List.of(
                Option.builder().optionText("Unresponsive").build(),
                Option.builder().optionText("Severe Bleeding").build(),
                Option.builder().optionText("Chest Pain").build(),
                Option.builder().optionText("Difficulty Breathing").build(),
                Option.builder().optionText("Other").build()
        );
        Question q3 = Question.builder()
                .question("What Are The Patient's Current Symptoms Or Condition?")
                .questionnaire(questionnaire)
                .createdAt(now)
                .updatedAt(now)
                .options(new ArrayList<>(options3))
                .build();

        List<Option> options4 = List.of(
                Option.builder().optionText("Accident").build(),
                Option.builder().optionText("Heart Attack").build(),
                Option.builder().optionText("Stroke Injury").build(),
                Option.builder().optionText("Other").build()
        );
        Question q4 = Question.builder()
                .question("What Is The Nature Of The Emergency?")
                .questionnaire(questionnaire)
                .createdAt(now)
                .updatedAt(now)
                .options(new ArrayList<>(options4))
                .build();

        // Question 5
        List<Option> options5 = List.of(
                Option.builder().optionText("Neonatal").build(),
                Option.builder().optionText("Severe Bleeding").build(),
                Option.builder().optionText("Trauma Care").build(),
                Option.builder().optionText("Oxygen Supply").build(),
                Option.builder().optionText("Other").build()
        );
        Question q5 = Question.builder()
                .question("Are There Any Additional Services Needed?")
                .questionnaire(questionnaire)
                .createdAt(now)
                .updatedAt(now)
                .options(new ArrayList<>(options5))
                .build();


        List<Question> allQuestions = List.of(q1,q2,q3,q4,q5);
        questionRepository.saveAll(allQuestions);
    }

    private Questionnaire getOrCreateQuestionnaire() {
        Optional<Questionnaire> existingQuestionnaire = questionnaireRepository.findById(1L);

        if (existingQuestionnaire.isPresent()) {
            return existingQuestionnaire.get();
        } else {
            Instant now = Instant.now();
            Questionnaire newQuestionnaire = Questionnaire.builder()
                    .id(1L)
                    .title("Emergency Services Questionnaire")
                    .description("Standard questionnaire for emergency medical services")
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            return questionnaireRepository.save(newQuestionnaire);
        }
    }
}
package com.aktic.indussahulatbackend.service.questionnaire;

import com.aktic.indussahulatbackend.model.common.eventState.QuestionnaireFilledState;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.request.AnswerDTO;
import com.aktic.indussahulatbackend.model.request.SaveResponseDTO;
import com.aktic.indussahulatbackend.repository.incidentEvent.IncidentEventRepository;
import com.aktic.indussahulatbackend.repository.option.OptionRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.questionnaire.QuestionnaireRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;
    private final IncidentEventRepository incidentEventRepository;
    private final OptionRepository optionRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final AuthService authService;

    @Transactional
    public ResponseEntity<ApiResponse<List<Question>>> getQuestionnaire(Long questionnaireId) {
        try{
            Optional<Questionnaire> questionnaire = questionnaireRepository.findById(questionnaireId);
            if(questionnaire.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Questionnaire not found", null));
            }

            Optional<List<Question>> questions = questionRepository.findByQuestionnaireId(questionnaireId);
            if(questions != null && questions.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Questions not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Questionnaire fetched successfully", questions.get()));
        }catch (Exception e) {
            log.error("Error fetching questionnaire: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error fetching questionnaire", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Void>> saveResponse(Long questionnaireId, SaveResponseDTO saveResponseDTO) {
        try {
            Patient patient = (Patient) authService.getCurrentUser();

            Questionnaire questionnaire = questionnaireRepository.findById(questionnaireId)
                    .orElseThrow(() -> new NoSuchElementException("Questionnaire not found"));

            // Fetch Event
            IncidentEvent event = incidentEventRepository.findById(saveResponseDTO.getEventId())
                    .orElseThrow(() -> new NoSuchElementException("Incident Event not found"));

            // Fetch Questions for the given Questionnaire
            List<Question> questions = questionRepository.findByQuestionnaireId(questionnaireId)
                    .orElseThrow(() -> new NoSuchElementException("Questions not found"));

            // Validate each answer
            List<Response> responsesToSave = new ArrayList<>();

            for (AnswerDTO answerDTO : saveResponseDTO.getResponse()) {
                // Validate Question ID
                Question question = questions.stream()
                        .filter(q -> q.getId().equals(answerDTO.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Invalid question ID: " + answerDTO.getQuestionId()));

                // Fetch and validate Option IDs
                List<Option> selectedOptions = optionRepository.findAllById(answerDTO.getOptionIds());
                if (selectedOptions.size() != answerDTO.getOptionIds().size()) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "One or more option IDs are invalid", null));
                }

                // Ensure all selected options belong to the given question
                boolean allValid = selectedOptions.stream().allMatch(question.getOptions()::contains);
                if (!allValid) {
                    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "One or more options do not belong to the question", null));
                }

                // Create and save response entity
                Response responseEntity = Response.builder()
                        .id(idGenerator.nextId())
                        .event(event)
                        .patient(patient)
                        .question(question)
                        .response(selectedOptions)
                        .build();

                responsesToSave.add(responseEntity);
            }

            event.nextState(new QuestionnaireFilledState());
            // Save All Responses
            responseRepository.saveAll(responsesToSave);
            incidentEventRepository.save(event);

            return ResponseEntity.ok(new ApiResponse<>(true, "Responses saved successfully", null));

        }catch (NoSuchElementException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error saving response: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Error saving response", null));
        }
    }
}

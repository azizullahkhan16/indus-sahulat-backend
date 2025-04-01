package com.aktic.indussahulatbackend.controller.patient.questionnaire;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.request.AnswerDTO;
import com.aktic.indussahulatbackend.model.request.SaveResponseDTO;
import com.aktic.indussahulatbackend.service.questionnaire.QuestionnaireService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientQuestionController {

   private final QuestionnaireService questionnaireService;

   @GetMapping( "/get-questionnaire/{questionnaireId}")
   public ResponseEntity<ApiResponse<List<Question>>> getQuestionnaire(@PathVariable Long questionnaireId) {
       return questionnaireService.getQuestionnaire(questionnaireId);
   }

   @PostMapping("/save-response/{questionnaireId}")
    public ResponseEntity<ApiResponse<Void>> saveResponse(@PathVariable Long questionnaireId,
                                                          @Valid @RequestBody SaveResponseDTO response) {
         return questionnaireService.saveResponse(questionnaireId, response);
    }


}


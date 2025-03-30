package com.aktic.indussahulatbackend.controller.patient.questionnaire;

import com.aktic.indussahulatbackend.model.response.QuestionForm;
import com.aktic.indussahulatbackend.service.questionnaire.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientQuestionController {

   private final QuestionService questionService;

   @GetMapping(value = "/get-questions")
   public ResponseEntity<List<QuestionForm>> getQuestions() {
       List<QuestionForm> questions = questionService.getAllQuestions();
       return ResponseEntity.ok(questions);
   }

}


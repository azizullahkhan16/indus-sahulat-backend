package com.aktic.indussahulatbackend.controller;

import com.aktic.indussahulatbackend.model.response.QuestionForm;
import com.aktic.indussahulatbackend.service.questionnaire.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuestionController {

   @Autowired
   private QuestionService questionService;



   @GetMapping(value = "/getQuestionForm")
   public ResponseEntity<List<QuestionForm>> getQuestions() {
       List<QuestionForm> questions = questionService.getAllQuestions();
       return ResponseEntity.ok(questions);
   }


}


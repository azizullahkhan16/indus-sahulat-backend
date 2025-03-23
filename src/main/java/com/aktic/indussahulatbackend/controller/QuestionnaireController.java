package com.aktic.indussahulatbackend.controller;

import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.response.FormResponse;
import com.aktic.indussahulatbackend.service.questionnaire.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class QuestionnaireController {

   @Autowired
   private  QuestionnaireService questionnaireService;


    @GetMapping(value = "/getForm")
    public ResponseEntity<List<FormResponse>> getForm() {
        List<Question> questions = questionnaireService.form(1);

        // Convert Question entities to FormResponse DTOs
        List<FormResponse> formResponses = questions.stream()
                .map(question -> {
                    FormResponse formResponse = new FormResponse();
                    formResponse.setQuestion(question.getQuestion());
                    formResponse.setOptions(question.getOptions());
                    return formResponse;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(formResponses, HttpStatus.OK);
    }



}


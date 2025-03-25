package com.aktic.indussahulatbackend.model.request;

import lombok.Data;
import java.util.List;

@Data
public class FormRequest {
    private List<Answer> answerList;

    @Data
    public static class Answer {
        private Long questionId;
        private List<String> responses;
    }
}

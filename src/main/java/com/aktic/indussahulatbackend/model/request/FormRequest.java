package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class FormRequest {
    @Valid // forces Spring to validate each item inside the list.
    private List<Answer> answerList;

    @Data
    public static class Answer {

        @NotNull(message = "Question ID cannot be null")
        private Long questionId;

        // responses can be null or empty, so no validation
        private List<String> responses;
    }
}

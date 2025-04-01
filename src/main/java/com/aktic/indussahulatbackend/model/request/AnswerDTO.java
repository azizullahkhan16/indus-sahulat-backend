package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnswerDTO {
    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotNull(message = "Option ids is required")
    private List<Long> optionIds;
}

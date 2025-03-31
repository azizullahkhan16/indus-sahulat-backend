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
public class SaveResponseDTO {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Response is required")
    private List<AnswerDTO> response;
}

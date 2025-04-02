package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateAssignmentDTO {
    @NotNull(message = "Status is required")
    @Pattern(regexp = "^(ACCEPTED|REJECTED)$", message = "Status must be either ACCEPTED or REJECTED")
    private String status;
}

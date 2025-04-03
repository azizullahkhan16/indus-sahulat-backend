package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusRequestDTO {
    @NotNull(message = "ID cannot be null")
    private Long eventAmbulanceAssignmentId;
}

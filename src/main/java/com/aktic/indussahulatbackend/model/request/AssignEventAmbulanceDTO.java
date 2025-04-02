package com.aktic.indussahulatbackend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AssignEventAmbulanceDTO {
    @NotNull(message = "Event ID cannot be null.")
    private Long eventId;

    @NotNull(message = "Ambulance ID cannot be null.")
    private Long ambulanceAssignmentId;
}

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
public class SendAdmitRequestDTO {
    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Hospital ID is required")
    private Long hospitalId;
}

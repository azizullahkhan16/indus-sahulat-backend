package com.aktic.indussahulatbackend.model.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AmbulanceAssignmentRequest
{
    @NotNull(message = "Ambulance ID cannot be null")
    private Long ambulanceId;

    @NotNull(message = "Driver ID cannot be null")
    private Long driverId;
}

package com.aktic.indussahulatbackend.model.request;


import lombok.Data;

@Data
public class AmbulanceAssignmentRequest
{
    private Long ambulance_providerId;
    private Long ambulance_id;
    private Long driver_id;
}

package com.aktic.indussahulatbackend.model.response.ambulance;

import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AmbulanceDTO
{
    private AmbulanceType ambulanceType;
    private Long companyId;
    private Long id;
    private String color;
    private String image;
    private String licensePlate;
    private String make;
    private String model;
    private String year;
}

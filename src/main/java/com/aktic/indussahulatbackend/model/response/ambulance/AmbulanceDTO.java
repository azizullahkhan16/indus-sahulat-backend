package com.aktic.indussahulatbackend.model.response.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.response.actor.CompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AmbulanceDTO
{
    private Long id;
    private CompanyDTO companyDTO;
    private String make;
    private String model;
    private String year;
    private String licensePlate;
    private AmbulanceType ambulanceType;
    private String color;
    private String image;
    private Instant createdAt;
    private Instant updatedAt;

    public AmbulanceDTO(Ambulance ambulance)
    {
        this.id = ambulance.getId();
        this.companyDTO = new CompanyDTO(ambulance.getCompany());
        this.make = ambulance.getMake();
        this.model = ambulance.getModel();
        this.year = ambulance.getYear();
        this.licensePlate = ambulance.getLicensePlate();
        this.ambulanceType = ambulance.getAmbulanceType();
        this.color = ambulance.getColor();
        this.image = ambulance.getImage();
        this.createdAt = ambulance.getCreatedAt();
        this.updatedAt = ambulance.getUpdatedAt();
    }

}

package com.aktic.indussahulatbackend.model.response.ambulanceAssignment;


import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AmbulanceAssignmentDTO {
    private Long id;
    private AmbulanceProvider ambulanceProvider;
    private Ambulance ambulance;
    private AmbulanceDriver ambulanceDriver;
    private Boolean isActive;
}

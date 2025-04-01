package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO extends UserDTO {
    private String gender;
    private String bloodGroup;
    private Float height;
    private Float weight;

    public PatientDTO(Patient patient) {
        super(patient);
        this.bloodGroup = patient.getBloodType().getValue();
        this.gender = patient.getGender().getValue();
        this.height = patient.getHeight();
        this.weight = patient.getWeight();
    }


}

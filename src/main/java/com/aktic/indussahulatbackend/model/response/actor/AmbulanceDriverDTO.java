package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceDriverDTO extends UserDTO {
    private CompanyDTO company;

    public AmbulanceDriverDTO(AmbulanceDriver ambulanceDriver) {
        super(ambulanceDriver);
        this.company = new CompanyDTO(ambulanceDriver.getCompany());
    }
}

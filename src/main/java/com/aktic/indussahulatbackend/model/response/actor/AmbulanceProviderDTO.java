package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceProviderDTO extends UserDTO {
    private CompanyDTO company;

    public AmbulanceProviderDTO(AmbulanceProvider ambulanceProvider) {
        super(ambulanceProvider);
        this.company = new CompanyDTO(ambulanceProvider.getCompany());
    }
}

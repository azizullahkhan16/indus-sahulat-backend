package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceProviderInfo extends UserInfo{
    private CompanyInfo company;

    public AmbulanceProviderInfo(AmbulanceProvider ambulanceProvider) {
        super(ambulanceProvider);
        this.company = new CompanyInfo(ambulanceProvider.getCompany());
    }
}

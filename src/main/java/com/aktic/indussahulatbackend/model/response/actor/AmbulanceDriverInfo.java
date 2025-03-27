package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceDriverInfo extends UserInfo{
    private CompanyInfo company;

    public AmbulanceDriverInfo(AmbulanceDriver ambulanceDriver) {
        super(ambulanceDriver);
        this.company = new CompanyInfo(ambulanceDriver.getCompany());
    }
}

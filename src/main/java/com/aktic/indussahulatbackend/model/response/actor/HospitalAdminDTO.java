package com.aktic.indussahulatbackend.model.response.actor;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.entity.HospitalAdmin;
import com.aktic.indussahulatbackend.model.response.HospitalDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalAdminDTO extends UserDTO {
    private HospitalDTO hospital;

    public HospitalAdminDTO(HospitalAdmin hospitalAdmin) {
        super(hospitalAdmin);
        this.hospital = new HospitalDTO(hospitalAdmin.getHospital());
    }
}

package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.common.Location;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HospitalDTO {
    private Long id;
    private String name;
    private Location address;
    private String phone;
    private String email;
    private String website;
    private String image;
    private Instant createdAt;
    private Instant updatedAt;

    public HospitalDTO(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.address = hospital.getAddress();
        this.phone = hospital.getPhone();
        this.email = hospital.getEmail();
        this.website = hospital.getWebsite();
        this.image = hospital.getImage();
        this.createdAt = hospital.getCreatedAt();
        this.updatedAt = hospital.getUpdatedAt();
    }
}

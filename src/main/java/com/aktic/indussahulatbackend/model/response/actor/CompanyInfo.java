package com.aktic.indussahulatbackend.model.response.actor;


import com.aktic.indussahulatbackend.model.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfo {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String website;
    private String description;
    private String logo;
    private Instant createdAt;
    private Instant updatedAt;

    public CompanyInfo(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.email = company.getEmail();
        this.phone = company.getPhone();
        this.website = company.getWebsite();
        this.description = company.getDescription();
        this.logo = company.getLogo();
        this.createdAt = company.getCreatedAt();
        this.updatedAt = company.getUpdatedAt();
    }
}

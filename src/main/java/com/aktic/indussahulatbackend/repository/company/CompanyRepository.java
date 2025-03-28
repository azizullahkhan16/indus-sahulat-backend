package com.aktic.indussahulatbackend.repository.company;

import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String indusHealthcare);
}

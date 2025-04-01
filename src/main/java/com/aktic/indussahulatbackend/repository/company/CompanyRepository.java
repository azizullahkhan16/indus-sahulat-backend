package com.aktic.indussahulatbackend.repository.company;

import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByName(String indusHealthcare);

    Optional<Company> findByName(String indusHealthcare);
}

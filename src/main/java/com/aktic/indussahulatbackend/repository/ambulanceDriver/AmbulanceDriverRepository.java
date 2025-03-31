package com.aktic.indussahulatbackend.repository.ambulanceDriver;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AmbulanceDriverRepository extends JpaRepository<AmbulanceDriver, Long> {
    Optional<AmbulanceDriver> findByPhone(String phone);

    boolean existsByEmail(String email);

    List<AmbulanceDriver> findByCompany(Company providerCompany);
}

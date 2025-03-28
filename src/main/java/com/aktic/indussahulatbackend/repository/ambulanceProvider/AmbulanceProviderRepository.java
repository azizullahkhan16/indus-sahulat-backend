package com.aktic.indussahulatbackend.repository.ambulanceProvider;

import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmbulanceProviderRepository extends JpaRepository<AmbulanceProvider, Long> {
    Optional<AmbulanceProvider> findByPhone(String phone);
}

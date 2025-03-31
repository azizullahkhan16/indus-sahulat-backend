package com.aktic.indussahulatbackend.repository.ambulanceProvider;

import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmbulanceProviderRepository extends JpaRepository<AmbulanceProvider, Long> {
    Optional<AmbulanceProvider> findByPhone(String phone);

    boolean existsByEmail(String email);
}

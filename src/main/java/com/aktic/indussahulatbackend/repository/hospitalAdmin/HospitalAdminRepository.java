package com.aktic.indussahulatbackend.repository.hospitalAdmin;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.entity.HospitalAdmin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalAdminRepository extends JpaRepository<HospitalAdmin, Long> {
    Optional<HospitalAdmin> findByPhone(@NotBlank(message = "Phone number is required") String phone);
}

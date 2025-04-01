package com.aktic.indussahulatbackend.repository.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.Company;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long>
{
    List<Ambulance> findByAmbulanceType(AmbulanceType ambulanceType);
    boolean existsByLicensePlate(String s);
   Page<Ambulance> findByCompany(Company company, Pageable pageable);

}

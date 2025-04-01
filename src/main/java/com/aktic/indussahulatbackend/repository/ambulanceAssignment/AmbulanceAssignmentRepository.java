package com.aktic.indussahulatbackend.repository.ambulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment,Long>
{

    boolean existsByAmbulanceAndIsActiveTrue(Ambulance ambulance);

    boolean existsByAmbulanceDriverAndIsActiveTrue(AmbulanceDriver ambulanceDriver);

    Page<AmbulanceAssignment> findByIsActiveTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE AmbulanceAssignment a SET a.isActive = false WHERE a.id = :id")
    void unassignById(Long id);

    AmbulanceAssignment findByAmbulanceDriverAndIsActiveTrue(AmbulanceDriver ambulanceDriver);
}

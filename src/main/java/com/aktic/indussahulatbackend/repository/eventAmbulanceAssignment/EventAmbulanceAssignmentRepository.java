package com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventAmbulanceAssignmentRepository extends JpaRepository<EventAmbulanceAssignment, Long> {

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignment(AmbulanceAssignment assignment);

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignmentAndStatus(AmbulanceAssignment ambulanceAssignment, RequestStatus requestStatus);

    Optional<EventAmbulanceAssignment> findByIdAndAmbulanceProviderCompany(Long id, Company providerCompany);
}

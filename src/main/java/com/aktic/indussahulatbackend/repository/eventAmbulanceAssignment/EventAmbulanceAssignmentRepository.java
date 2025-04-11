package com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventAmbulanceAssignmentRepository extends JpaRepository<EventAmbulanceAssignment, Long> {

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignment(AmbulanceAssignment assignment);

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignmentAndStatus(AmbulanceAssignment ambulanceAssignment, RequestStatus requestStatus);
}

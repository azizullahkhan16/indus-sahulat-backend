package com.aktic.indussahulatbackend.repository.eventHospitalAssignment;

import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventHospitalAssignmentRepository extends JpaRepository<EventHospitalAssignment, Long> {
    Page<EventHospitalAssignment> findByHospitalAndStatus(Hospital hospital, RequestStatus requestStatus, Pageable pageable);

    Optional<EventHospitalAssignment> findByIdAndHospital(Long eventHospitalAssignmentId, Hospital hospital);

    Optional<EventHospitalAssignment> findByEventAndHospitalAndStatus(IncidentEvent event, Hospital hospital, RequestStatus requestStatus);
}

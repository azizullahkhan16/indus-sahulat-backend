package com.aktic.indussahulatbackend.repository.eventHospitalAssignment;

import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventHospitalAssignment;
import com.aktic.indussahulatbackend.model.entity.Hospital;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventHospitalAssignmentRepository extends JpaRepository<EventHospitalAssignment, Long> {
    Page<EventHospitalAssignment> findByHospitalAndStatus(Hospital hospital, RequestStatus requestStatus, Pageable pageable);

    Optional<EventHospitalAssignment> findByIdAndHospital(Long eventHospitalAssignmentId, Hospital hospital);

    Optional<EventHospitalAssignment> findByEventAndHospitalAndStatus(IncidentEvent event, Hospital hospital, RequestStatus requestStatus);

    @Transactional
    @Query(value = """
            UPDATE event_hospital_assignments
            SET status = :assignmentStatus
            WHERE id = :id
            RETURNING *
            """, nativeQuery = true)
    Optional<EventHospitalAssignment> updateEventHospitalAssignmentStatusById(
            @Param("id") Long id,
            @Param("assignmentStatus") String assignmentStatus
    );

    Optional<EventHospitalAssignment> findByEventAndHospitalAndStatusIn(IncidentEvent event, Hospital hospital, List<RequestStatus> requested);
}

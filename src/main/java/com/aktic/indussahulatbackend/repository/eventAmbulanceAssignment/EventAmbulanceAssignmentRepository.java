package com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import com.aktic.indussahulatbackend.model.enums.RequestStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAmbulanceAssignmentRepository extends JpaRepository<EventAmbulanceAssignment, Long> {

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignment(AmbulanceAssignment assignment);

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignmentAndStatus(AmbulanceAssignment ambulanceAssignment, RequestStatus requestStatus);

    Optional<EventAmbulanceAssignment> findByIdAndAmbulanceProviderCompany(Long id, Company providerCompany);

    @Transactional
    @Query(value = """
            WITH updated_assignment AS (
                UPDATE event_ambulance_assignments
                SET status = :assignmentStatus
                WHERE id = :id
                RETURNING *
            ),
            updated_event AS (
                UPDATE incident_events
                SET status = :eventStatus
                WHERE id = (SELECT event_id FROM updated_assignment)
            )
            SELECT * FROM updated_assignment
            """, nativeQuery = true)
    Optional<EventAmbulanceAssignment> updateStatusAndEventStatus(
            @Param("id") Long id,
            @Param("assignmentStatus") String assignmentStatus,
            @Param("eventStatus") String eventStatus
    );

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignmentAndStatusIn(AmbulanceAssignment ambulanceAssignment, List<RequestStatus> requested);
}

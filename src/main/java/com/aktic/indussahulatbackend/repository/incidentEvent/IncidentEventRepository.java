package com.aktic.indussahulatbackend.repository.incidentEvent;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent, Long> {
    Page<IncidentEvent> findByStatus(EventStatus eventStatus, Pageable pageable);

    Optional<IncidentEvent> findFirstByPatientAndStatusNotIn(Patient patient, List<EventStatus> list, Sort createdAt);

    Optional<IncidentEvent> findByIdAndStatus(Long eventId, EventStatus eventStatus);

    Optional<IncidentEvent> findByIdAndStatusNotIn(Long eventId, List<EventStatus> patientAdmitted);
}

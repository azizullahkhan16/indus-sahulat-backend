package com.aktic.indussahulatbackend.repository.incidentEvent;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent,Long> {
    Page<IncidentEvent> findByStatus(EventStatus eventStatus, Pageable pageable);
}

package com.aktic.indussahulatbackend.repository.incidentEvent;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent,Long> {
}

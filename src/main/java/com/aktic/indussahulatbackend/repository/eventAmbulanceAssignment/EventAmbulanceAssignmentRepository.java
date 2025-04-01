package com.aktic.indussahulatbackend.repository.eventAmbulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import com.aktic.indussahulatbackend.model.entity.EventAmbulanceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventAmbulanceAssignmentRepository extends JpaRepository<EventAmbulanceAssignment, Long> {

    Optional<EventAmbulanceAssignment> findByAmbulanceAssignment(AmbulanceAssignment assignment);
}

package com.aktic.indussahulatbackend.repository.ambulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.AmbulanceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceAssignmentRepository extends JpaRepository<AmbulanceAssignment,Long>
{

}

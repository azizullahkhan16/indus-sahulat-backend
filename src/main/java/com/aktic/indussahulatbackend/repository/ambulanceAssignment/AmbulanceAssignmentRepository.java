package com.aktic.indussahulatbackend.repository.ambulanceAssignment;

import com.aktic.indussahulatbackend.model.entity.Ambulance_Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceAssignmentRepository extends JpaRepository<Ambulance_Assignment,Long>
{

}

package com.aktic.indussahulatbackend.repository.hospital;

import com.aktic.indussahulatbackend.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>
{
    boolean existsByName(String name);
}

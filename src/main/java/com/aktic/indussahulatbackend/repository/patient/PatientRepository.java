package com.aktic.indussahulatbackend.repository.patient;

import com.aktic.indussahulatbackend.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>
{
    Optional<Patient> findById(Long id);
}

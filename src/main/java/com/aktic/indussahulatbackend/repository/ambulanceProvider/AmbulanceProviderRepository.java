package com.aktic.indussahulatbackend.repository.ambulanceProvider;

import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmbulanceProviderRepository extends JpaRepository<AmbulanceProvider, Long>
{

}

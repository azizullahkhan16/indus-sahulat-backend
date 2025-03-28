package com.aktic.indussahulatbackend.security;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PatientRepository patientRepository;

    private final AmbulanceDriverRepository driverRepository;

    private final AmbulanceProviderRepository providerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // Find user in different repositories
        Optional<Patient> patient = patientRepository.findByPhone(phone);
        if (patient.isPresent()) {
            return UserPrincipal.create(patient.get());
        }

        Optional<AmbulanceDriver> driver = driverRepository.findByPhone(phone);
        if (driver.isPresent()) {
            Hibernate.initialize(driver.get().getCompany());
            return UserPrincipal.create(driver.get());
        }

        Optional<AmbulanceProvider> provider = providerRepository.findByPhone(phone);
        if (provider.isPresent()) {
            Hibernate.initialize(provider.get().getCompany());
            return UserPrincipal.create(provider.get());
        }

        throw new UsernameNotFoundException("User not found with email: " + phone);
    }
}

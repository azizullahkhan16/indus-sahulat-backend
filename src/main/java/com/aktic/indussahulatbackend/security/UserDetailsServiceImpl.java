package com.aktic.indussahulatbackend.security;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.entity.AmbulanceProvider;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.repository.hospitalAdmin.HospitalAdminRepository;
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
public class UserDetailsServiceImpl implements CustomUserDetailsService {

    private final PatientRepository patientRepository;
    private final AmbulanceDriverRepository driverRepository;
    private final AmbulanceProviderRepository providerRepository;
    private final HospitalAdminRepository hospitalAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("User role is required to fetch details");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsernameAndRole(String phone, String userRole) throws UsernameNotFoundException {
        return findUserByPhoneAndRole(phone, userRole);
    }

    private UserDetails findUserByPhoneAndRole(String phone, String userRole) {
        System.out.println("Finding user by phone: " + phone + " and role: " + userRole);
        return switch (userRole.toUpperCase()) {
            case "ROLE_PATIENT" -> patientRepository.findByPhone(phone)
                    .map(UserPrincipal::create)
                    .orElseThrow(() -> new UsernameNotFoundException("Patient not found with phone: " + phone));
            case "ROLE_AMBULANCE_DRIVER" -> driverRepository.findByPhone(phone)
                    .map(driver -> {
                        Hibernate.initialize(driver.getCompany());
                        return UserPrincipal.create(driver);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Ambulance driver not found with phone: " + phone));
            case "ROLE_AMBULANCE_PROVIDER" -> providerRepository.findByPhone(phone)
                    .map(provider -> {
                        Hibernate.initialize(provider.getCompany());
                        return UserPrincipal.create(provider);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Ambulance provider not found with phone: " + phone));
            case "ROLE_HOSPITAL_ADMIN" -> hospitalAdminRepository.findByPhone(phone)
                    .map(driver -> {
                        Hibernate.initialize(driver.getHospital());
                        return UserPrincipal.create(driver);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Hospital Admin not found with phone: " + phone));
            default -> throw new IllegalArgumentException("Invalid user role: " + userRole);
        };
    }
}

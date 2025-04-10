package com.aktic.indussahulatbackend.service.auth;

import com.aktic.indussahulatbackend.model.common.UserBase;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.request.*;
import com.aktic.indussahulatbackend.model.request.actor.AmbulanceDriverRegisterDTO;
import com.aktic.indussahulatbackend.model.request.actor.AmbulanceProviderRegisterDTO;
import com.aktic.indussahulatbackend.model.request.actor.HospitalAdminRegisterDTO;
import com.aktic.indussahulatbackend.model.request.actor.PatientRegisterDTO;
import com.aktic.indussahulatbackend.model.response.AuthenticationResponse;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceProviderDTO;
import com.aktic.indussahulatbackend.model.response.actor.HospitalAdminDTO;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import com.aktic.indussahulatbackend.repository.ambulanceDriver.AmbulanceDriverRepository;
import com.aktic.indussahulatbackend.repository.ambulanceProvider.AmbulanceProviderRepository;
import com.aktic.indussahulatbackend.repository.company.CompanyRepository;
import com.aktic.indussahulatbackend.repository.hospital.HospitalRepository;
import com.aktic.indussahulatbackend.repository.hospitalAdmin.HospitalAdminRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.role.RoleRepository;
import com.aktic.indussahulatbackend.security.CustomUserDetailsService;
import com.aktic.indussahulatbackend.security.UserDetailsServiceImpl;
import com.aktic.indussahulatbackend.security.UserPrincipal;
import com.aktic.indussahulatbackend.service.jwt.JwtService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SnowflakeIdGenerator idGenerator;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AmbulanceProviderRepository ambulanceProviderRepository;
    private final CompanyRepository companyRepository;
    private final AmbulanceDriverRepository ambulanceDriverRepository;
    private final HospitalAdminRepository hospitalAdminRepository;
    private final HospitalRepository hospitalRepository;
    private final CustomUserDetailsService userDetailsService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Transactional
    public ResponseEntity<ApiResponse<String>> patientRegister(PatientRegisterDTO request) {
        try{
            // check if the patient already exists with the given phone
            Optional<Patient> existingPatient = patientRepository.findByPhone(request.getPhone());
            if (existingPatient.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone is already in use", null));
            }

            Role userRole = roleRepository.findByRoleName("ROLE_PATIENT");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Role not found", null));
            }

            Patient patient = Patient.builder()
                    .id(idGenerator.nextId())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .CNIC(request.getCNIC())
                    .weight(request.getWeight())
                    .height(request.getHeight())
                    .bloodType(request.getBloodType())
                    .gender(request.getGender())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(userRole)
                    .build();

            patientRepository.save(patient);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Patient registered successfully", null));

        }catch (Exception e) {
            log.error("Error occurred while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AuthenticationResponse>> patientLogin(AuthenticationRequest request) {
        try{
            Optional<Patient> optionalPatient = patientRepository.findByPhone(request.getPhone());
            if (optionalPatient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone not found", null));
            }

            Patient patient = optionalPatient.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(patient, request.getPassword())
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }


            String token = jwtService.generateToken(patient);
            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(new PatientDTO(patient), token)));


        }catch (Exception e) {
            log.error("Error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> ambulanceProviderRegister(@Valid AmbulanceProviderRegisterDTO request) {
        try{
            // check if the ambulance provider already exists with the given phone
            Optional<AmbulanceProvider> existingAmbulanceProvider = ambulanceProviderRepository.findByPhone(request.getPhone());
            if (existingAmbulanceProvider.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone is already in use", null));
            }

            // verify if the company exists
            Optional<Company> company = companyRepository.findById(request.getCompanyId());
            if (company.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Company not found", null));
            }

            Role userRole = roleRepository.findByRoleName("ROLE_AMBULANCE_PROVIDER");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Role not found", null));
            }

            AmbulanceProvider ambulanceProvider = AmbulanceProvider.builder()
                    .id(idGenerator.nextId())
                    .company(company.get())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .CNIC(request.getCNIC())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(userRole)
                    .build();

            ambulanceProviderRepository.save(ambulanceProvider);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Ambulance provider registered successfully", null));

        }catch (Exception e) {
            log.error("Error occurred while registering ambulance provider", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AuthenticationResponse>> ambulanceProviderLogin(@Valid AuthenticationRequest request) {
        try{
            Optional<AmbulanceProvider> optionalAmbulanceProvider = ambulanceProviderRepository.findByPhone(request.getPhone());
            if (optionalAmbulanceProvider.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone not found", null));
            }

            AmbulanceProvider ambulanceProvider = optionalAmbulanceProvider.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                ambulanceProvider,
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }

            String token = jwtService.generateToken(ambulanceProvider);
            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(new AmbulanceProviderDTO(ambulanceProvider), token)));

        }catch (Exception e) {
            log.error("Error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> ambulanceDriverRegister(@Valid AmbulanceDriverRegisterDTO request) {
        try{
            // check if the ambulance drvier already exists with the given phone
            Optional<AmbulanceDriver> existingAmbulanceDriver = ambulanceDriverRepository.findByPhone(request.getPhone());
            if(existingAmbulanceDriver.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone is already in use", null));
            }

            // verify if the company exists
            Optional<Company> company = companyRepository.findById(request.getCompanyId());
            if (company.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Company not found", null));
            }

            Role userRole = roleRepository.findByRoleName("ROLE_AMBULANCE_DRIVER");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Role not found", null));
            }

            AmbulanceDriver ambulanceDriver = AmbulanceDriver.builder()
                    .id(idGenerator.nextId())
                    .company(company.get())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .CNIC(request.getCNIC())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(userRole)
                    .build();

            ambulanceDriverRepository.save(ambulanceDriver);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Ambulance driver registered successfully", null));
        }catch (Exception e) {
            log.error("Error occurred while registering ambulance driver", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AuthenticationResponse>> ambulanceDriverLogin(@Valid AuthenticationRequest request) {
        try{
            Optional<AmbulanceDriver> optionalAmbulanceDriver = ambulanceDriverRepository.findByPhone(request.getPhone());
            if (optionalAmbulanceDriver.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone not found", null));
            }

            AmbulanceDriver ambulanceDriver = optionalAmbulanceDriver.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                ambulanceDriver,
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }

            String token = jwtService.generateToken(ambulanceDriver);

            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(new AmbulanceDriverDTO(ambulanceDriver), token)));

        }catch (Exception e) {
            log.error("Error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    public UserBase getCurrentUser() {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userPrincipal.getUser();
        } catch (Exception e) {
            log.error("Error occurred while getting current user", e);
            return null;
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> hospitalAdminRegister(@Valid HospitalAdminRegisterDTO request) {
        try{
            // check if the hospital admin already exists with the given phone
            Optional<HospitalAdmin> existingHospitalAdmin = hospitalAdminRepository.findByPhone(request.getPhone());
            if(existingHospitalAdmin.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone is already in use", null));
            }

            // verify if the hospital exists
            Optional<Hospital> hospital = hospitalRepository.findById(request.getHospitalId());
            if (hospital.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Hospital not found", null));
            }

            Role userRole = roleRepository.findByRoleName("ROLE_HOSPITAL_ADMIN");
            if (userRole == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Role not found", null));
            }

            HospitalAdmin hospitalAdmin = HospitalAdmin.builder()
                    .id(idGenerator.nextId())
                    .hospital(hospital.get())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .CNIC(request.getCNIC())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(userRole)
                    .build();

            hospitalAdminRepository.save(hospitalAdmin);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Hospital Admin registered successfully", null));
        }catch (Exception e) {
            log.error("Error occurred while registering ambulance driver", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<AuthenticationResponse>> hospitalAdminLogin(@Valid AuthenticationRequest request) {
        try{
            Optional<HospitalAdmin> optionalHospitalAdmin = hospitalAdminRepository.findByPhone(request.getPhone());
            if (optionalHospitalAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone not found", null));
            }

            HospitalAdmin hospitalAdmin = optionalHospitalAdmin.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                hospitalAdmin,
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }

            String token = jwtService.generateToken(hospitalAdmin);

            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(new HospitalAdminDTO(hospitalAdmin), token)));

        }catch (Exception e) {
            log.error("Error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }
}

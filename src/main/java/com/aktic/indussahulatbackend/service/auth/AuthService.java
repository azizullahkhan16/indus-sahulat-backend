package com.aktic.indussahulatbackend.service.auth;

import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.model.entity.Role;
import com.aktic.indussahulatbackend.model.request.AuthenticationRequest;
import com.aktic.indussahulatbackend.model.request.PatientRegisterRequest;
import com.aktic.indussahulatbackend.model.response.AuthenticationResponse;
import com.aktic.indussahulatbackend.model.response.UserInfo;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.role.RoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<ApiResponse<String>> patientRegister(PatientRegisterRequest request) {
        try{
            // check if the patient already exists with the given phone
            Optional<Patient> existingPatient = patientRepository.findByPhone(request.getPhone());
            if (existingPatient.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone is already in use", null));
            }

            Role userRole = roleRepository.findByRoleName("PATIENT");
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
                    .age(request.getAge())
                    .role(userRole)
                    .build();

            patientRepository.save(patient);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Patient registered successfully", null));

        }catch (Exception e) {
            log.error("Error occurred while registering user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }

    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(AuthenticationRequest request) {
        try{
            Optional<Patient> optionalPatient = patientRepository.findByPhone(request.getPhone());
            if (optionalPatient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "Phone not found", null));
            }

            Patient patient = optionalPatient.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getPhone(),
                                request.getPassword()
                        )
                );
            } catch (BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid password", null));
            }

            String token = jwtService.generateToken(patient.getPhone());
            UserInfo userInfo = new UserInfo(patient);

            return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully",
                    new AuthenticationResponse(userInfo, token)));


        }catch (Exception e) {
            log.error("Error occurred while logging in", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Error", null));
        }
    }
}

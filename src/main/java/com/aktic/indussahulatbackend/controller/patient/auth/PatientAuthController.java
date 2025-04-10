package com.aktic.indussahulatbackend.controller.patient.auth;

import com.aktic.indussahulatbackend.model.request.AuthenticationRequest;
import com.aktic.indussahulatbackend.model.request.actor.PatientRegisterDTO;
import com.aktic.indussahulatbackend.model.response.AuthenticationResponse;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/patient")
@RequiredArgsConstructor
public class PatientAuthController {

    private final AuthService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> patientRegister(
            @Valid @RequestBody PatientRegisterDTO request
    ) {
        return service.patientRegister(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> patientLogin(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.patientLogin(request);
    }
}

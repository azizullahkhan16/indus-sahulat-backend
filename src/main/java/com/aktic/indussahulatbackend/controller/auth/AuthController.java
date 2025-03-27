package com.aktic.indussahulatbackend.controller.auth;

import com.aktic.indussahulatbackend.model.request.AuthenticationRequest;
import com.aktic.indussahulatbackend.model.request.PatientRegisterRequest;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping(value = "/patient/register")
    public ResponseEntity<ApiResponse<String>> patientRegister(
            @Valid @RequestBody PatientRegisterRequest request
    ) {
        return service.patientRegister(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.login(request);
    }
}

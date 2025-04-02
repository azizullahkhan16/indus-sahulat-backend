package com.aktic.indussahulatbackend.controller.hospitalAdmin.auth;

import com.aktic.indussahulatbackend.model.request.AmbulanceDriverRegisterRequest;
import com.aktic.indussahulatbackend.model.request.AuthenticationRequest;
import com.aktic.indussahulatbackend.model.request.HospitalAdminRegisterRequest;
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
@RequestMapping("/api/auth/hospital-admin")
@RequiredArgsConstructor
public class HospitalAdminAuthController {
    private final AuthService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> hospitalAdminRegister(
            @Valid @RequestBody HospitalAdminRegisterRequest request
    ) {
        return service.hospitalAdminRegister(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> hospitalAdminLogin(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.hospitalAdminLogin(request);
    }
}

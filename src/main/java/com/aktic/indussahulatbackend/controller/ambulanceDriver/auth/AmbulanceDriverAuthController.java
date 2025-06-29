package com.aktic.indussahulatbackend.controller.ambulanceDriver.auth;


import com.aktic.indussahulatbackend.model.request.actor.AmbulanceDriverRegisterDTO;
import com.aktic.indussahulatbackend.model.request.AuthenticationRequest;
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
@RequestMapping("/api/auth/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverAuthController {

    private final AuthService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> ambulanceDriverRegister(
            @Valid @RequestBody AmbulanceDriverRegisterDTO request
    ) {
        return service.ambulanceDriverRegister(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> ambulanceDriverLogin(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.ambulanceDriverLogin(request);
    }
}

package com.aktic.indussahulatbackend.controller.ambulanceProvider.auth;


import com.aktic.indussahulatbackend.model.request.actor.AmbulanceProviderRegisterDTO;
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
@RequestMapping("/api/auth/ambulance-provider")
@RequiredArgsConstructor
public class AmbulanceProviderAuthController {

    private final AuthService service;

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> ambulanceProviderRegister(
            @Valid @RequestBody AmbulanceProviderRegisterDTO request
    ) {
        return service.ambulanceProviderRegister(request);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> ambulanceProviderLogin(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return service.ambulanceProviderLogin(request);
    }
}

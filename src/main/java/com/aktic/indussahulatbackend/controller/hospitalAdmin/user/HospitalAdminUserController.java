package com.aktic.indussahulatbackend.controller.hospitalAdmin.user;

import com.aktic.indussahulatbackend.model.response.actor.HospitalAdminDTO;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import com.aktic.indussahulatbackend.service.user.UserService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospital-admin")
@RequiredArgsConstructor
public class HospitalAdminUserController {
    private final UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<ApiResponse<HospitalAdminDTO>> getHospitalAdminInfo() {
        return userService.getHospitalAdminInfo();
    }
}

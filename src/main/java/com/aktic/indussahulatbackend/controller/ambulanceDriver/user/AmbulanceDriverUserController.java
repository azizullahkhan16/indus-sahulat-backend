package com.aktic.indussahulatbackend.controller.ambulanceDriver.user;

import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverInfo;
import com.aktic.indussahulatbackend.service.user.UserService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverUserController {

    private final UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<ApiResponse<AmbulanceDriverInfo>> getAmbulanceDriverInfo() {
        return userService.getAmbulanceDriverInfo();
    }
}

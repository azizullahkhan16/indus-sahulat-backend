package com.aktic.indussahulatbackend.controller.ambulanceDriver.user;

import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.service.user.UserService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ambulance-driver")
@RequiredArgsConstructor
public class AmbulanceDriverUserController {

    private final UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<ApiResponse<AmbulanceDriverDTO>> getAmbulanceDriverInfo() {
        return userService.getAmbulanceDriverInfo();
    }
}

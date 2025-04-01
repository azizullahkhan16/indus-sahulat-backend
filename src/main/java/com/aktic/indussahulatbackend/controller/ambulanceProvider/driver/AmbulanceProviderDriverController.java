package com.aktic.indussahulatbackend.controller.ambulanceProvider.driver;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.service.ambulanceDriver.AmbulanceDriverService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ambulance-provider")
@RequiredArgsConstructor
public class AmbulanceProviderDriverController {

    private final AmbulanceDriverService ambulanceDriverService;

    @GetMapping("get-drivers")
    public ResponseEntity<ApiResponse<Page<AmbulanceDriverDTO>>> getAllDrivers(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer limit
    )
    {
        return ambulanceDriverService.getAllUnassignedDriver(pageNumber, limit);
    }

    @GetMapping("get-driver/{id}")
    public ResponseEntity<ApiResponse<AmbulanceDriverDTO>> getDriverById(@PathVariable Long id){
        return ambulanceDriverService.getDriver(id);
    }
}

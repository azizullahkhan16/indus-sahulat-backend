package com.aktic.indussahulatbackend.controller.ambulanceProvider.driver;

import com.aktic.indussahulatbackend.model.entity.AmbulanceDriver;
import com.aktic.indussahulatbackend.model.response.ambulanceDriver.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.service.ambulanceDriver.AmbulanceDriverService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-provider")
@RequiredArgsConstructor
public class AmbulanceProviderDriverController {

    private final AmbulanceDriverService ambulanceDriverService;

    @GetMapping("get-drivers")
    public ResponseEntity<ApiResponse<List<AmbulanceDriverDTO>>> getAllDrivers()
    {
        return ambulanceDriverService.getAllDriver();
    }

    @GetMapping("get-drvier/{id}")
    public ResponseEntity<ApiResponse<AmbulanceDriverDTO>> getDriverById(@PathVariable Long id){
        return ambulanceDriverService.getDriver(id);
    }
}

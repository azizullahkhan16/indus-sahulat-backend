package com.aktic.indussahulatbackend.controller.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.service.ambulance.AmbulanceService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance")
@RequiredArgsConstructor
public class AmbulanceController
{
    private final AmbulanceService ambulanceService;

    @PostMapping("/get-ambulances")
    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(@Valid @RequestBody FormRequest formRequest) {
        return ambulanceService.getAvailableAmbulances(formRequest);
    }
}


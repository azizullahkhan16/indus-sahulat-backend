package com.aktic.indussahulatbackend.controller.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.service.ambulance.AmbulanceService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ambulance")
public class AmbulanceController
{
    @Autowired
    private AmbulanceService ambulanceService;

    @PostMapping("/getAvailable")
    public ResponseEntity<ApiResponse<List<AmbulanceDTO>>> getAvailableAmbulances(@RequestBody FormRequest formRequest)
    {
        List<AmbulanceDTO> ambulanceList = ambulanceService.getAvailableAmbulances(formRequest);
        ApiResponse<List<AmbulanceDTO>> apiResponse = new ApiResponse<>(true,"Ambulances fetched successfully",ambulanceList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}


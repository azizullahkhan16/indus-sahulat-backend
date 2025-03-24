package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.Patient;
import com.aktic.indussahulatbackend.model.entity.Response;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmbulanceService
{

    @Autowired
    private AmbulanceRepository ambulanceRepository;
    @Autowired
    private PatientRepository patientRepository;

    public AmbulanceType determineCategory(FormRequest formRequest)
    {
        boolean isConscious = true;
        boolean hasChestPain = false;

        List<Response> responses = formRequest.getResponseList();

        for (Response response: responses)
        {
            Long patientId = response.getPatient().getId();
//            Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new PatientNotFoundException(PatientNotFoundException.DEFAULT_MESSAGE));
            String questionId = response.getQuestion().getId().toString();
            String answer = response.getResponse();

            switch (questionId) {
                case "1" :
                    if ("no".equalsIgnoreCase(answer)) {
                        isConscious = false;
                    }
                    break;
                case "2" :
                    if ("yes".equalsIgnoreCase(answer))
                    {
                        hasChestPain = true;
                    }
                    break;
            }
        }
        if (!isConscious || hasChestPain) {
            return AmbulanceType.ADVANCED;
        } else {
            return AmbulanceType.NORMAL;
        }
    }

    public List<AmbulanceDTO> getAvailableAmbulances(FormRequest formRequest)
    {
        AmbulanceType category = determineCategory(formRequest);
        List<Ambulance> ambulanceList = ambulanceRepository.findByAmbulanceType(category);

        return ambulanceList.stream().map(
                ambulance -> new AmbulanceDTO(
                        ambulance.getAmbulanceType(),
                        ambulance.getCompany().getId(),
                        ambulance.getId(),
                        ambulance.getColor(),
                        ambulance.getImage(),
                        ambulance.getLicensePlate(),
                        ambulance.getMake(),
                        ambulance.getModel(),
                        ambulance.getYear()
                )
        ).collect(Collectors.toList());
    }
}
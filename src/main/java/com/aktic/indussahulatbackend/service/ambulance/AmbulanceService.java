package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.model.entity.Ambulance;
import com.aktic.indussahulatbackend.model.entity.Response;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmbulanceService
{

    public AmbulanceType determineCategory(FormRequest formRequest)
    {
        List<Response> responses = formRequest.getResponseList();
        boolean isConscious = true;
        boolean hasChestPain = false;

        for (Response response: responses)
        {
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

//    public List<Ambulance> getAvailableAmbulances()
//    {
//
//    }
}
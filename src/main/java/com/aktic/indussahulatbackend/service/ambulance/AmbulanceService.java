package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.QuestionNotFoundException;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
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
    @Autowired
    private QuestionRepository questionRepository;

    public AmbulanceType determineCategory(FormRequest formRequest)
    {
        boolean isUnconscious = false;
        boolean hasChestPain = false;
        boolean hasSevreBleeding = false;
        boolean hasbreathingIssue = false;
        boolean hasDiabetes =false;
        boolean hasHeartDisease = false;
        boolean hasCancer = false;
        boolean hasAllergies = false;
        boolean hasAccident = false;
        boolean hadHeartAttack = false;
        boolean hadStrokeInjury = false;
        boolean hasTrauma = false;
        boolean isNeonatal = false;
        boolean needOxygenSupply = false;


        List<FormRequest.Answer> answers = formRequest.getAnswerList();

        for (FormRequest.Answer answer: answers)
        {
            Long patientId = 1L;
//            Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new PatientNotFoundException(PatientNotFoundException.DEFAULT_MESSAGE));
            Long questionId = answer.getQuestionId();
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new QuestionNotFoundException(QuestionNotFoundException.DEFAULT_MESSAGE));
            List<Option> availableOptions = question.getOptions();
            List<String> options = availableOptions.stream()
                    .map(Option::getOptionText)
                    .toList();
            List<String> response = answer.getResponses();

            for (String res : response)
            {
                if (options.contains(res))
                {
                    switch (res)
                    {
                        case "Unresponsive":
                            isUnconscious = true;
                            break;
                        case "Chest Pain":
                            hasChestPain = true;
                            break;
                        case "Severe Bleeding":
                            hasSevreBleeding = true;
                            break;
                        case "Difficulty Breathing":
                            hasbreathingIssue = true;
                            break;
                        case "Diabetes":
                            hasDiabetes = true;
                            break;
                        case "Heart Disease":
                            hasHeartDisease = true;
                            break;
                        case "Cancer":
                            hasCancer = true;
                            break;
                        case "Accident":
                            hasAccident = true;
                            break;
                        case "Heart Attack":
                            hadHeartAttack = true;
                            break;
                        case "Storke Injury":
                            hadStrokeInjury = true;
                            break;
                        case "Neonatal":
                            isNeonatal = true;
                            break;
                        case "Trauma Care":
                            hasTrauma = true;
                            break;
                        case "Oxygen Supply":
                            needOxygenSupply = true;
                            break;
                        case "Other":
                            System.out.println("Other symptoms.");
                            break;
                        default:
                            System.out.println("Unknown response: " + response);
                    }
                }
            }
        }
        if (isUnconscious || hasChestPain || hasbreathingIssue || hasSevreBleeding) {
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
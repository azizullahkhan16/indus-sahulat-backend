package com.aktic.indussahulatbackend.service.ambulance;

import com.aktic.indussahulatbackend.exception.customexceptions.AmbulanceNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.PatientNotFoundException;
import com.aktic.indussahulatbackend.exception.customexceptions.QuestionNotFoundException;
import com.aktic.indussahulatbackend.model.entity.*;
import com.aktic.indussahulatbackend.model.enums.AmbulanceType;
import com.aktic.indussahulatbackend.model.request.FormRequest;
import com.aktic.indussahulatbackend.model.response.ambulance.AmbulanceDTO;
import com.aktic.indussahulatbackend.repository.ambulance.AmbulanceRepository;
import com.aktic.indussahulatbackend.repository.patient.PatientRepository;
import com.aktic.indussahulatbackend.repository.question.QuestionRepository;
import com.aktic.indussahulatbackend.repository.response.ResponseRepository;
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
    @Autowired
    private ResponseRepository responseRepository;

    public AmbulanceType determineCategory(FormRequest formRequest) {
        boolean isUnconscious = false;
        boolean hasChestPain = false;
        boolean hasSevereBleeding = false;
        boolean hasBreathingIssue = false;
        boolean hasDiabetes = false;
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
        Long patientId = 1L;

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(PatientNotFoundException.DEFAULT_MESSAGE));

        for (FormRequest.Answer answer : answers) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new QuestionNotFoundException(QuestionNotFoundException.DEFAULT_MESSAGE));

            List<String> availableOptions = question.getOptions().stream()
                    .map(Option::getOptionText)
                    .toList();

            responseRepository.save(Response.builder()
                    .patient(patient)
                    .question(question)
                    .response("response")
                    .build());

            for (String res : answer.getResponses()) {
                if (availableOptions.contains(res)) {
                    switch (res) {
                        case "Unresponsive" -> isUnconscious = true;
                        case "Chest Pain" -> hasChestPain = true;
                        case "Severe Bleeding" -> hasSevereBleeding = true;
                        case "Difficulty Breathing" -> hasBreathingIssue = true;
                        case "Diabetes" -> hasDiabetes = true;
                        case "Heart Disease" -> hasHeartDisease = true;
                        case "Cancer" -> hasCancer = true;
                        case "Accident" -> hasAccident = true;
                        case "Heart Attack" -> hadHeartAttack = true;
                        case "Stroke Injury" -> hadStrokeInjury = true;
                        case "Neonatal" -> isNeonatal = true;
                        case "Trauma Care" -> hasTrauma = true;
                        case "Oxygen Supply" -> needOxygenSupply = true;
                        case "Other" -> System.out.println("Other symptoms.");
                        default -> System.out.println("Unknown response: " + res);
                    }
                }
            }
        }

        return (isUnconscious || hasChestPain || hasBreathingIssue || hasSevereBleeding) ?
                AmbulanceType.ADVANCED : AmbulanceType.NORMAL;
    }

    public List<AmbulanceDTO> getAvailableAmbulances(FormRequest formRequest) {
//        if (formRequest == null || formRequest.getAnswerList() == null || formRequest.getAnswerList().isEmpty()) {
//            throw new InvalidFormRequestException("FormRequest cannot be null or empty");
//        }
        AmbulanceType category = determineCategory(formRequest);
//        if (category == null) {
//            throw new InvalidFormRequestException("Could not determine category from formRequest");
//        }
        List<Ambulance> ambulanceList = ambulanceRepository.findByAmbulanceType(category);


        if (ambulanceList == null || ambulanceList.isEmpty()) {
            throw new AmbulanceNotFoundException("No available ambulances found for the given category.");
        }

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
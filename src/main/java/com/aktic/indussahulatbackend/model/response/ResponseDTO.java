package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Option;
import com.aktic.indussahulatbackend.model.entity.Question;
import com.aktic.indussahulatbackend.model.entity.Response;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseDTO {
    private Long id;
    private Long eventId;
    private Long patientId;
    private QuestionDTO question;
    private List<Option> response;
    private Instant createdAt;
    private Instant updatedAt;

    public ResponseDTO(Response response) {
        this.id = response.getId();
        this.eventId = response.getEvent().getId();
        this.patientId = response.getPatient().getId();
        this.question = new QuestionDTO(response.getQuestion());
        this.response = response.getResponse();
        this.createdAt = response.getCreatedAt();
        this.updatedAt = response.getUpdatedAt();
    }
}

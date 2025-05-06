package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Chatroom;
import com.aktic.indussahulatbackend.model.response.actor.AmbulanceDriverDTO;
import com.aktic.indussahulatbackend.model.response.actor.HospitalAdminDTO;
import com.aktic.indussahulatbackend.model.response.actor.PatientDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatroomDTO {
    private Long id;
    private Long eventId;
    private PatientDTO patient;
    private AmbulanceDriverDTO ambulanceDriver;
    private HospitalAdminDTO hospitalAdmin;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public ChatroomDTO(Chatroom chatroom) {
        this.id = chatroom.getId();
        this.eventId = chatroom.getEvent().getId();
        this.patient = chatroom.getPatient() != null ? new PatientDTO(chatroom.getPatient()) : null;
        this.ambulanceDriver = chatroom.getAmbulanceDriver() != null ? new AmbulanceDriverDTO(chatroom.getAmbulanceDriver()) : null;
        this.hospitalAdmin = chatroom.getHospitalAdmin() != null ? new HospitalAdminDTO(chatroom.getHospitalAdmin()) : null;
        this.isActive = chatroom.getIsActive();
        this.createdAt = chatroom.getCreatedAt();
        this.updatedAt = chatroom.getUpdatedAt();
    }

}

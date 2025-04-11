package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public class PatientAdmittedState implements EventState{
    @Override
    public EventStatus getStatus() {
        return EventStatus.PATIENT_ADMITTED;
    }

    @Override
    public void next(IncidentEvent event, EventState nextState) {
        throw new IllegalStateException("Event is already in the Patient Admitted state and cannot move to the next state.");
    }

    @Override
    public void cancel(IncidentEvent event) {
        throw new IllegalStateException("Event can not be cancelled once the patient is admitted.");
    }
}

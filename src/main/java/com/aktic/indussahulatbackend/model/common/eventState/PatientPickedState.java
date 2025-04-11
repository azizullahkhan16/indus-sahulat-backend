package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public class PatientPickedState implements EventState{
    @Override
    public EventStatus getStatus() {
        return EventStatus.PATIENT_PICKED;
    }

    @Override
    public void next(IncidentEvent event, EventState nextState) {
        if (!(event.getState() instanceof PatientPickedState)) {
            throw new IllegalStateException("Event is not in the correct state to move to the next state.");
        }
        if (!(nextState instanceof PatientAdmittedState)) {
            throw new IllegalArgumentException("Invalid next state.");
        }
        event.setState(nextState);
        event.setStatus(nextState.getStatus());
    }

    @Override
    public void cancel(IncidentEvent event) {
        throw new IllegalStateException("Event can not be cancelled once the patient is picked.");
    }
}

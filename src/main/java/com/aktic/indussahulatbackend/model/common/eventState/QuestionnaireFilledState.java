package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public class QuestionnaireFilledState implements EventState{
    @Override
    public EventStatus getStatus() {
        return EventStatus.QUESTIONNAIRE_FILLED;
    }

    @Override
    public void next(IncidentEvent event, EventState nextState) {
        if(!(event.getState() instanceof QuestionnaireFilledState)) {
            throw new IllegalStateException("Event is not in the correct state to move to the next state.");
        }
        if(!(nextState instanceof AmbulanceAssignedState)) {
            throw new IllegalArgumentException("Invalid next state.");
        }
        event.setState(nextState);
        event.setStatus(nextState.getStatus());
    }

    @Override
    public void cancel(IncidentEvent event) {
        if(!(event.getState() instanceof QuestionnaireFilledState)) {
            throw new IllegalStateException("Event is not in the correct state to move to be cancelled.");
        }
        event.setStatus(EventStatus.CANCELLED);
        event.setState(new CancelledState());
    }
}

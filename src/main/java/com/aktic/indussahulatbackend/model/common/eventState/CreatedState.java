package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public class CreatedState implements EventState {
    @Override
    public EventStatus getStatus() {
        return EventStatus.CREATED;
    }

    @Override
    public void next(IncidentEvent event, EventState nextState) {
        if(!(event.getState() instanceof CreatedState)) {
            throw new IllegalStateException("Event is not in the correct state to move to the next state.");
        }
        if(!(nextState instanceof QuestionnaireFilledState)) {
            throw new IllegalArgumentException("Invalid next state.");
        }
        event.setState(nextState);
        event.setStatus(nextState.getStatus());
    }

    @Override
    public void cancel(IncidentEvent event) {
        if(!(event.getState() instanceof CreatedState)) {
            throw new IllegalStateException("Event is not in the correct state to be cancelled.");
        }
        event.setState(new CancelledState());
        event.setStatus(EventStatus.CANCELLED);
    }
}

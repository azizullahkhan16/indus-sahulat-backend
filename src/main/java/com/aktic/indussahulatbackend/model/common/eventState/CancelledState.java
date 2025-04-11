package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public class CancelledState implements EventState {
    @Override
    public EventStatus getStatus() {
        return EventStatus.CANCELLED;
    }

    @Override
    public void next(IncidentEvent event, EventState nextState) {
        throw new IllegalStateException("Event is cancelled and cannot move to the next state.");
    }

    @Override
    public void cancel(IncidentEvent event) {
        throw new IllegalStateException("Event is already cancelled.");
    }
}

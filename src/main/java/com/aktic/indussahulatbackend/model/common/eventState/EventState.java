package com.aktic.indussahulatbackend.model.common.eventState;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.enums.EventStatus;

public interface EventState {
    EventStatus getStatus();
    void next(IncidentEvent event, EventState nextState);
    void cancel(IncidentEvent event);
}

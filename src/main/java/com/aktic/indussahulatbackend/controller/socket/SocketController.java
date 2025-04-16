package com.aktic.indussahulatbackend.controller.socket;

import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.request.LocationDTO;
import com.aktic.indussahulatbackend.model.response.IncidentEventDTO;
import com.aktic.indussahulatbackend.model.response.LiveEventUpdateDTO;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SocketController {
    private final SocketService socketService;

    @MessageMapping("/event/live-location/{eventId}")
    public void updateLiveLocation(@DestinationVariable Long eventId,
                                   @Valid LocationDTO locationDTO) {
        socketService.updateLiveLocation(eventId, locationDTO);
    }

}

package com.aktic.indussahulatbackend.controller.socket;

import com.aktic.indussahulatbackend.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SocketController {
    private final SocketService socketService;

    @MessageMapping("/join/event/{eventId}")
    public void joinIncidentEvent(@DestinationVariable Long eventId) {
        socketService.joinIncidentEvent(eventId);
    }
}

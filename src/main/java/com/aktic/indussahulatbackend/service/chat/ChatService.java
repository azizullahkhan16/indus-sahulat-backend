package com.aktic.indussahulatbackend.service.chat;

import com.aktic.indussahulatbackend.model.entity.Chatroom;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.repository.chatroom.ChatroomRepository;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final SnowflakeIdGenerator idGenerator;

    public Chatroom createChatroomForEvent(IncidentEvent event) {
        try {
            Chatroom chatroom = Chatroom.builder()
                    .id(idGenerator.nextId())
                    .event(event)
                    .patient(event.getPatient())
                    .build();
            log.info("Creating chatroom for event: " + event.getId());
            return chatroomRepository.save(chatroom);
        } catch (Exception e) {
            log.error("Error creating chatroom for event: " + event.getId(), e);
            throw new RuntimeException("Error creating chatroom for event: " + event.getId(), e);
        }
    }
}

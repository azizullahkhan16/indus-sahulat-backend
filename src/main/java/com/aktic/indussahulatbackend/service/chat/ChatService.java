package com.aktic.indussahulatbackend.service.chat;

import com.aktic.indussahulatbackend.model.common.UserBase;
import com.aktic.indussahulatbackend.model.entity.Chatroom;
import com.aktic.indussahulatbackend.model.entity.IncidentEvent;
import com.aktic.indussahulatbackend.model.entity.Message;
import com.aktic.indussahulatbackend.model.enums.MessageSenderType;
import com.aktic.indussahulatbackend.model.request.AddMessageDTO;
import com.aktic.indussahulatbackend.model.response.MessageDTO;
import com.aktic.indussahulatbackend.repository.chatroom.ChatroomRepository;
import com.aktic.indussahulatbackend.repository.message.MessageRepository;
import com.aktic.indussahulatbackend.service.auth.AuthService;
import com.aktic.indussahulatbackend.service.socket.SocketService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import com.aktic.indussahulatbackend.util.SnowflakeIdGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final AuthService authService;
    private final MessageRepository messageRepository;
    private final SocketService socketService;

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

    @Transactional
    public ResponseEntity<ApiResponse<MessageDTO>> addMessage(Long chatroomId, @Valid AddMessageDTO addMessageDTO) {
        try {
            UserBase sender = authService.getCurrentUser();

            Chatroom chatroom = chatroomRepository.findByIdAndIsActiveTrue(chatroomId)
                    .orElseThrow(() -> new RuntimeException("Chatroom not found"));

            boolean isAuthorized =
                    (chatroom.getPatient() != null && chatroom.getPatient().getId().equals(sender.getId())) ||
                            (chatroom.getAmbulanceDriver() != null && chatroom.getAmbulanceDriver().getId().equals(sender.getId())) ||
                            (chatroom.getHospitalAdmin() != null && chatroom.getHospitalAdmin().getId().equals(sender.getId()));

            if (!isAuthorized) {
                return new ResponseEntity<>(new ApiResponse<>(false, "You are not authorized to send messages in this chatroom", null), HttpStatus.UNAUTHORIZED);
            }

            String trimmedMessage = addMessageDTO.getMessage().trim();

            MessageSenderType senderType;
            switch (sender.getRole().getRoleName()) {
                case "ROLE_PATIENT" -> senderType = MessageSenderType.PATIENT;
                case "ROLE_AMBULANCE_DRIVER" -> senderType = MessageSenderType.AMBULANCE_DRIVER;
                case "ROLE_HOSPITAL_ADMIN" -> senderType = MessageSenderType.HOSPITAL_ADMIN;
                default -> {
                    return new ResponseEntity<>(new ApiResponse<>(false, "Invalid sender type", null), HttpStatus.BAD_REQUEST);
                }
            }

            Message message = Message.builder()
                    .id(idGenerator.nextId())
                    .chatroom(chatroom)
                    .senderId(sender.getId())
                    .senderType(senderType)
                    .message(trimmedMessage)
                    .build();

            message = messageRepository.save(message);

            MessageDTO messageDTO = new MessageDTO(message, sender);

            socketService.sendNewMessageToChatroom(messageDTO);

            return new ResponseEntity<>(new ApiResponse<>(true, "Message added successfully", messageDTO), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error adding message: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "Chatroom has been deactivated!", null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error adding new message: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "Error adding new message", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<Page<MessageDTO>>> getMessages(Long chatroomId, Integer pageNumber, Integer limit) {
        try {
            UserBase currentUser = authService.getCurrentUser();

            Chatroom chatroom = chatroomRepository.findById(chatroomId)
                    .orElseThrow(() -> new RuntimeException("Chatroom not found"));

            boolean isAuthorized =
                    (chatroom.getPatient() != null && chatroom.getPatient().getId().equals(currentUser.getId())) ||
                            (chatroom.getAmbulanceDriver() != null && chatroom.getAmbulanceDriver().getId().equals(currentUser.getId())) ||
                            (chatroom.getHospitalAdmin() != null && chatroom.getHospitalAdmin().getId().equals(currentUser.getId())) ||
                            (chatroom.getEvent() != null &&
                                    chatroom.getEvent().getAmbulanceProvider() != null &&
                                    chatroom.getEvent().getAmbulanceProvider().getId().equals(currentUser.getId()));

            if (!isAuthorized) {
                return new ResponseEntity<>(new ApiResponse<>(false, "You are not authorized to view messages in this chatroom", null), HttpStatus.UNAUTHORIZED);
            }

            int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
            int size = (limit != null && limit > 0) ? limit : 50;
            Pageable pageable = PageRequest.of(page, size);

            Page<Object[]> resultPage = messageRepository.fetchMessagesWithSenderInfo(chatroomId, pageable);

            Page<MessageDTO> messageDTOPage = resultPage.map(MessageDTO::new);

            return ResponseEntity.ok(new ApiResponse<>(true, "Messages fetched successfully", messageDTOPage));
        } catch (Exception e) {
            log.error("Error getting messages: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ApiResponse<>(false, "Error getting messages", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

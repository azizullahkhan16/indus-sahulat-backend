package com.aktic.indussahulatbackend.controller.chat;

import com.aktic.indussahulatbackend.model.request.AddMessageDTO;
import com.aktic.indussahulatbackend.model.response.MessageDTO;
import com.aktic.indussahulatbackend.service.chat.ChatService;
import com.aktic.indussahulatbackend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/add-message/{chatroomId}")
    public ResponseEntity<ApiResponse<MessageDTO>> addMessage(
            @PathVariable Long chatroomId,
            @Valid @RequestBody AddMessageDTO addMessageDTO
    ) {
        return chatService.addMessage(chatroomId, addMessageDTO);
    }


    @GetMapping("/get-messages/{chatroomId}")
    public ResponseEntity<ApiResponse<Page<MessageDTO>>> getMessages(
            @PathVariable Long chatroomId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer limit
    ) {
        return chatService.getMessages(chatroomId, pageNumber, limit);
    }
}

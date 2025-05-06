package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageDTO {
    private Long id;
    private Long chatroomId;
    private Map<String, Object> sender;
    private String senderType;
    private String message;
    private String createdAt;
    private String updatedAt;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.chatroomId = message.getChatroom().getId();
        this.sender = Map.of(
                "id", message.getSenderId(),
                "type", message.getSenderType()
        );
        this.senderType = message.getSenderType().name();
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt().toString();
        this.updatedAt = message.getUpdatedAt().toString();
    }
}

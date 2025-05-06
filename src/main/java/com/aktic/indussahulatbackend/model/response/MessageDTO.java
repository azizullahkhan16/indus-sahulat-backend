package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.common.UserBase;
import com.aktic.indussahulatbackend.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
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

    public MessageDTO(Message message, UserBase sender) {
        this.id = message.getId();
        this.chatroomId = message.getChatroom().getId();

        Map<String, Object> senderMap = new LinkedHashMap<>();
        senderMap.put("id", message.getSenderId());
        senderMap.put("firstName", sender.getFirstName());
        senderMap.put("lastName", sender.getLastName());
        senderMap.put("image", sender.getImage()); // null-safe

        this.sender = senderMap;
        this.senderType = message.getSenderType().name();
        this.message = message.getMessage();
        this.createdAt = message.getCreatedAt().toString();
        this.updatedAt = message.getUpdatedAt().toString();
    }

    public MessageDTO(Object[] row) {
        this.id = ((Number) row[0]).longValue();
        this.chatroomId = ((Number) row[1]).longValue();
        Long senderId = ((Number) row[2]).longValue();
        this.senderType = (String) row[3];
        this.message = (String) row[4];
        this.createdAt = row[5].toString();
        this.updatedAt = row[6].toString();

        Map<String, Object> senderMap = new LinkedHashMap<>();
        senderMap.put("id", senderId);
        senderMap.put("firstName", row[7]);
        senderMap.put("lastName", row[8]);
        senderMap.put("image", row[9]);
        this.sender = senderMap;
    }
}

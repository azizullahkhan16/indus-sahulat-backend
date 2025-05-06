package com.aktic.indussahulatbackend.repository.message;

import com.aktic.indussahulatbackend.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(
            value = """
                    SELECT m.id AS message_id,
                           m.chatroom_id,
                           m.sender_id,
                           m.sender_type,
                           m.message,
                           m.created_at,
                           m.updated_at,
                           COALESCE(p.first_name, a.first_name, h.first_name) AS first_name,
                           COALESCE(p.last_name, a.last_name, h.last_name) AS last_name,
                           COALESCE(p.image, a.image, h.image) AS image
                    FROM messages m
                    LEFT JOIN patients p ON m.sender_type = 'PATIENT' AND m.sender_id = p.id
                    LEFT JOIN ambulance_drivers a ON m.sender_type = 'AMBULANCE_DRIVER' AND m.sender_id = a.id
                    LEFT JOIN hospital_admins h ON m.sender_type = 'HOSPITAL_ADMIN' AND m.sender_id = h.id
                    WHERE m.chatroom_id = :chatroomId
                    ORDER BY m.id DESC  
                    """,
            countQuery = """
                    SELECT COUNT(*) FROM messages m
                    WHERE m.chatroom_id = :chatroomId
                    """,
            nativeQuery = true
    )
    Page<Object[]> fetchMessagesWithSenderInfo(@Param("chatroomId") Long chatroomId, Pageable pageable);

}

package com.aktic.indussahulatbackend.repository.chatroom;

import com.aktic.indussahulatbackend.model.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByIdAndIsActiveTrue(Long chatroomId);
}

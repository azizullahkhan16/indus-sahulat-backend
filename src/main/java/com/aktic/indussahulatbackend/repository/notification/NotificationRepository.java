package com.aktic.indussahulatbackend.repository.notification;

import com.aktic.indussahulatbackend.model.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByReceiverId(Long id, Pageable pageable);

    Optional<Notification> findByIdAndReceiverId(Long notificationId, Long id);
}

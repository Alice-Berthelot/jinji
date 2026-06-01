package com.jinji.backend.repository;

import com.jinji.backend.model.entity.Notification;
import com.jinji.backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Long countByUser_IdAndReadFalse(Long userId);

    Optional<Notification> findByIdAndUser_Id(Long id, Long userId);
}
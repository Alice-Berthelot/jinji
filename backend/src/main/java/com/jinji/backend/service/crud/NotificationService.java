package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.NotificationMapper;
import com.jinji.backend.model.dto.response.NotificationDTO;
import com.jinji.backend.model.entity.Notification;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final UserService userService;
    private final NotificationMapper mapper;

    public NotificationService(NotificationRepository repository, UserService userService, NotificationMapper mapper) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
    }

    public void create(User user, String message) {
        Notification n = new Notification();
        n.setUser(user);
        n.setMessage(message);
        n.setCreatedAt(LocalDateTime.now());
        n.setRead(false);

        repository.save(n);
    }

    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {

        return repository.findByUser_IdOrderByCreatedAtDesc(userId, pageable)
                .map(mapper::toDto);
    }

    public long countUnread() {
        User currentUser = userService.getCurrentUser();
        return repository.countByUser_IdAndReadFalse(currentUser.getId());
    }

    public void markAsRead(Long id) {
        User currentUser = userService.getCurrentUser();

        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        if (!n.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Forbidden");
        }

        n.setRead(true);
        repository.save(n);
    }

    public void delete(Long id) {
        User currentUser = userService.getCurrentUser();

        Notification notification = repository
                .findByIdAndUser_Id(id, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        repository.delete(notification);
    }
}
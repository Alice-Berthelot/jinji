package com.jinji.backend.service.crud;

import com.jinji.backend.mapper.NotificationMapper;
import com.jinji.backend.model.dto.response.NotificationDTO;
import com.jinji.backend.model.entity.Notification;
import com.jinji.backend.model.entity.User;
import com.jinji.backend.model.enums.LeaveRequestDecision;
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


    // specific notifications

    public void notifyManagerDecisionToEmployee(Long leaveRequestId, User employeeUser, LeaveRequestDecision decision) {
        String message = switch (decision) {
            case APPROVED -> "Votre demande d'absence n° " + leaveRequestId + " a été acceptée par votre Manager. " +
                    "Elle a été transmise au service des Ressources humaines pour validation définitive.";
            case REJECTED -> "Votre demande d'absence n° " + leaveRequestId + " a été refusée par votre manager. " +
                    "Le service des Ressources humaines prendra prochainement une décision finale.";
        };

        create(employeeUser, message);
    }

    public void notifyFinalDecisionToEmployee(Long leaveRequestId, User employeeUser, LeaveRequestDecision decision) {
        String message = switch (decision) {
            case APPROVED -> "Votre demande d'absence n° " + leaveRequestId + " a été acceptée.";
            case REJECTED -> "Votre demande d'absence n° " + leaveRequestId + " a été refusée.";
        };

        create(employeeUser, message);
    }

    public void notifyManagerDecisionToHr(Long leaveRequestId, User employeeUser, LeaveRequestDecision decision, String employeeFullName) {
        String message = switch (decision) {
            case APPROVED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été acceptée par le manager. Il vous appartient de prendre une décision finale sur la demande.";
            case REJECTED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été refusée par le manager. Il vous appartient de prendre une décision finale sur la demande.";
        };

        create(employeeUser, message);
    }

    public void notifyManagerFinalDecisionToHr(Long leaveRequestId, User hrUser, LeaveRequestDecision decision, String employeeFullName) {
        String message = switch (decision) {
            case APPROVED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été acceptée par le manager.";
            case REJECTED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été refusée par le manager.";
        };

        create(hrUser, message);
    }

    public void notifyHrFinalDecisionToManager(Long leaveRequestId, User managerUser, LeaveRequestDecision decision, String employeeFullName) {
        String message = switch (decision) {
            case APPROVED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été validée par le service des Ressources humaines.";
            case REJECTED -> "La demande d'absence n° " + leaveRequestId + " de " + employeeFullName + " a été refusée par le service des Ressources humaines.";
        };

        create(managerUser, message);
    }

}
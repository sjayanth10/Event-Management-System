package com.example.Event_Management_System.Repository;


import com.example.Event_Management_System.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUsernameAndIsReadFalse(String username);
}

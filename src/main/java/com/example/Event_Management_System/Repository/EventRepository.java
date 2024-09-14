package com.example.Event_Management_System.Repository;

import com.example.Event_Management_System.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByReminderSentFalse();
}
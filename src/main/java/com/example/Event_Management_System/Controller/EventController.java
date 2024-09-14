package com.example.Event_Management_System.Controller;



import com.example.Event_Management_System.Entity.Event;
import com.example.Event_Management_System.Entity.Notification;
import com.example.Event_Management_System.Service.EventService;
import com.example.Event_Management_System.Service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final NotificationService notificationService;

    public EventController(EventService eventService, NotificationService notificationService) {
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    // Create an event
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        String username = getUsername();  // Replace with actual username retrieval logic
        Event createdEvent = eventService.createEvent(event, username);
        return ResponseEntity.ok(createdEvent);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        String username = getUsername();
        return ResponseEntity.ok(eventService.getAllEvents(username));
    }

    // Get an event by ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        String username = getUsername();
        Optional<Event> event = eventService.getEventById(id, username);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Send a reminder for an event
    @PostMapping("/{id}/send-reminder")
    public ResponseEntity<Void> sendReminder(@PathVariable Long id) {
        String username = getUsername();
        Optional<Event> event = eventService.getEventById(id, username);
        if (event.isPresent()) {
            eventService.markReminderSent(event.get(), username);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all unread notifications for the user
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        String username = getUsername();
        List<Notification> notifications = notificationService.getUnreadNotifications(username);
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PostMapping("/notifications/{id}/mark-as-read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    // Delete an event by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        String username = getUsername();
        Optional<Event> event = eventService.getEventById(id, username);
        if (event.isPresent()) {
            eventService.deleteEvent(id, username);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get pending reminders (events that haven't had reminders sent yet)
    @GetMapping("/pending-reminders")
    public ResponseEntity<List<Event>> getPendingReminders() {
        String username = getUsername();
        List<Event> pendingEvents = eventService.getPendingReminderEvents();
        return ResponseEntity.ok(pendingEvents);
    }

    // Placeholder for getting username, replace with actual implementation
    private String getUsername() {
        return "user@example.com";
    }
}
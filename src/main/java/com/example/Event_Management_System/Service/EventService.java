package com.example.Event_Management_System.Service;


import com.example.Event_Management_System.Entity.Event;
import com.example.Event_Management_System.Repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final UserActivityService userActivityService;

    public EventService(EventRepository eventRepository, EmailService emailService,
                        NotificationService notificationService, UserActivityService userActivityService) {
        this.eventRepository = eventRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
        this.userActivityService = userActivityService;
    }

    public Event createEvent(Event event, String username) {
        Event createdEvent = eventRepository.save(event);
        userActivityService.logActivity(username, "Created event: " + event.getName());
        return createdEvent;
    }

    public List<Event> getAllEvents(String username) {
        userActivityService.logActivity(username, "Viewed all events");
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id, String username) {
        userActivityService.logActivity(username, "Viewed event with id: " + id);
        return eventRepository.findById(id);
    }

    public void markReminderSent(Event event, String username) {
        event.setReminderSent(true);
        eventRepository.save(event);
        userActivityService.logActivity(username, "Sent reminder for event: " + event.getName());

        // Send email notification
        emailService.sendEmail(event.getOrganizerEmail(), "Reminder: Upcoming Event - " + event.getName(),
                "Reminder for your event scheduled on " + event.getDate());

        // Send in-app notification
        notificationService.sendNotification(event.getOrganizerEmail(), "Reminder for event: " + event.getName());
    }

    public void deleteEvent(Long id, String username) {
        eventRepository.deleteById(id);
        userActivityService.logActivity(username, "Deleted event with id: " + id);
    }

    public List<Event> getPendingReminderEvents() {
        return eventRepository.findByReminderSentFalse();
    }


}

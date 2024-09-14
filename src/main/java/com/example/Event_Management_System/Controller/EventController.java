package com.example.Event_Management_System.Controller;



import com.example.Event_Management_System.Entity.Event;
import com.example.Event_Management_System.Entity.Notification;
import com.example.Event_Management_System.Entity.Userinfo;
import com.example.Event_Management_System.Service.EventService;
import com.example.Event_Management_System.Service.JwtService;
import com.example.Event_Management_System.Service.NotificationService;
import com.example.Event_Management_System.Service.Userservices;
import com.example.Event_Management_System.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    private Userservices service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;


    private final EventService eventService;
    private final NotificationService notificationService;


    public EventController(EventService eventService, NotificationService notificationService) {
        this.eventService = eventService;
        this.notificationService = notificationService;
    }

    @PostMapping("/adduser")
    public String addNewUser(@RequestBody Userinfo user){
        return service.addUser(user);
    }
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }

    // Create an event
    @PostMapping("/addevent")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        String username = getUsername();  // Replace with actual username retrieval logic
        Event createdEvent = eventService.createEvent(event, username);
        return ResponseEntity.ok(createdEvent);
    }

    // Get all events
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Event>> getAllEvents() {
        String username = getUsername();
        return ResponseEntity.ok(eventService.getAllEvents(username));
    }

    // Get an event by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        String username = getUsername();
        Optional<Event> event = eventService.getEventById(id, username);
        return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Send a reminder for an event
    @PostMapping("/{id}/send-reminder")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        String username = getUsername();
        List<Notification> notifications = notificationService.getUnreadNotifications(username);
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PostMapping("/notifications/{id}/mark-as-read")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    // Delete an event by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
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
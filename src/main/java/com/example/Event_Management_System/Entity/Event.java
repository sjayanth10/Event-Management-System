package com.example.Event_Management_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String organizerEmail;
    private LocalDateTime dateTime;
    private String getDate;
    private boolean reminderSent;

    @ElementCollection
    private List<String> attendees;


    public String getDate() {
        return getDate;
    }
}

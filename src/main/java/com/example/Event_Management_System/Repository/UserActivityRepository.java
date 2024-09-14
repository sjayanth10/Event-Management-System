package com.example.Event_Management_System.Repository;


import com.example.Event_Management_System.Entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
}
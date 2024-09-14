package com.example.Event_Management_System.Service;




import com.example.Event_Management_System.Entity.UserActivity;
import com.example.Event_Management_System.Repository.UserActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    public UserActivityService(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    public void logActivity(String username, String action) {
        UserActivity activity = new UserActivity();
        activity.setUsername(username);
        activity.setAction(action);
        activity.setTimestamp(java.time.LocalDateTime.now());
        userActivityRepository.save(activity);
    }
}
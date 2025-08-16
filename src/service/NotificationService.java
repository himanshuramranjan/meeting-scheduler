package service;

import models.User;

import java.util.List;

public class NotificationService {
    private static NotificationService instance;
    private NotificationService() {}
    public static NotificationService getInstance() {
        if (instance == null) instance = new NotificationService();
        return instance;
    }

    public void notifyUsers(List<User> recipients, String message) {
        for (User user : recipients) {
            System.out.println("Notification to " + user.getName() + ": " + message);
        }
    }
}
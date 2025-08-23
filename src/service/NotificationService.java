package service;

import models.User;

import java.util.List;

public class NotificationService {
    private NotificationService() {}

    private static class NotificationServiceHolder {
        private static final NotificationService INSTANCE = new NotificationService();
    }

    public static NotificationService getInstance() {
        return NotificationServiceHolder.INSTANCE;
    }

    public void notifyUsers(List<User> recipients, String message) {
        for (User user : recipients) {
            System.out.println("Notification to " + user.getName() + ": " + message);
        }
    }
}
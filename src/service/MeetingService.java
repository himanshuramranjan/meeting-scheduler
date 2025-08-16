package service;

import models.Interval;
import models.Meeting;
import models.MeetingRoom;
import models.User;

import java.util.List;

public class MeetingService {
    private final NotificationService notificationService;

    public MeetingService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Meeting scheduleMeeting(String title, User organizer, List<User> participants, Interval interval, MeetingRoom room) {

        if (!room.checkAvailability(interval)) {
            throw new RuntimeException("Room not available.");
        }

        for (User participant : participants) {
            if (!participant.getCalendar().findAvailableSlots(interval).contains(interval)) {
                throw new RuntimeException("Participant not available: " + participant.getName());
            }
        }

        Meeting meeting = new Meeting(title, organizer, participants, interval, room);

        room.book(interval);

        for (User participant : participants) {
            participant.getCalendar().addMeeting(meeting);
        }

        String message = String.format(
                "New meeting scheduled: %s | From: %s To: %s",
                title,
                interval.start(),
                interval.end()
        );
        notificationService.notifyUsers(participants, message);
        notificationService.notifyUsers(participants, message);

        return meeting;
    }

    public void cancelMeeting(Meeting meeting) {
        meeting.cancel();
    }

    public void rescheduleMeeting(Meeting meeting, Interval newInterval) {
        if (!meeting.getRoom().checkAvailability(newInterval)) {
            throw new RuntimeException("Room not available for reschedule.");
        }

        for (User participant : meeting.getParticipants()) {
            if (!participant.getCalendar().findAvailableSlots(newInterval).contains(newInterval)) {
                throw new RuntimeException("Participant not available for reschedule: " + participant.getName());
            }
        }

        meeting.getRoom().book(newInterval);
        meeting.reschedule(newInterval);
    }
}
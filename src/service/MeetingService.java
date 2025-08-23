package service;

import models.Interval;
import models.Meeting;
import models.MeetingRoom;
import models.User;

import java.util.List;

public class MeetingService {
    private final NotificationService notificationService;

    private MeetingService() {
        this.notificationService = NotificationService.getInstance();
    }

    private static class MeetingServiceHolder {
        private static final MeetingService INSTANCE = new MeetingService();
    }

    public static MeetingService getInstance() {
        return MeetingServiceHolder.INSTANCE;
    }

    public Meeting scheduleMeeting(String title, User organizer, List<User> participants, Interval interval, MeetingRoom room) {

        if (!room.checkAvailability(interval)) {
            throw new RuntimeException("Room not available.");
        }

        validateParticipantsAvailability(participants, interval);

        Meeting meeting = new Meeting(title, organizer, participants, interval, room);

        room.book(interval);
        addMeetingToUsersCalendar(participants, meeting);
        notifyMeetingScheduled("New meeting scheduled: %s | From: %s To: %s", title, interval, participants);

        return meeting;
    }

    public void cancelMeeting(Meeting meeting) {
        meeting.cancel();

        meeting.getRoom().release(meeting.getInterval());

        // Remove meeting from participants’ calendars
        removeMeetingFromUsersCalendar(meeting);

        notifyMeetingScheduled("Meeting canceled: %s | Originally scheduled: %s to %s", meeting.getTitle(), meeting.getInterval(), meeting.getParticipants());
    }



    public void rescheduleMeeting(Meeting meeting, Interval newInterval) {
        if (!meeting.getRoom().checkAvailability(newInterval)) {
            throw new RuntimeException("Room not available for reschedule.");
        }

        validateParticipantsAvailability(meeting.getParticipants(), newInterval);

        // remove the older ones
        removeMeetingFromUsersCalendar(meeting);
        meeting.getRoom().release(meeting.getInterval());

        // add the new ones
        meeting.getRoom().book(newInterval);
        meeting.reschedule();
        addMeetingToUsersCalendar(meeting.getParticipants(), meeting);

        notifyMeetingScheduled("Meeting rescheduled: %s | New Time: %s to %s", meeting.getTitle(), newInterval, meeting.getParticipants());
    }

    private static void addMeetingToUsersCalendar(List<User> participants, Meeting meeting) {
        for (User participant : participants) {
            participant.getCalendar().addMeeting(meeting);
        }
    }

    private static void removeMeetingFromUsersCalendar(Meeting meeting) {
        for (User participant : meeting.getParticipants()) {
            participant.getCalendar().removeMeeting(meeting);
        }
    }

    private void validateParticipantsAvailability(List<User> participants, Interval interval) {
        for (User participant : participants) {
            if (!participant.getCalendar().isSlotAvailable(interval).contains(interval)) {
                throw new RuntimeException("Participant not available: " + participant.getName());
            }
        }
    }

    private void notifyMeetingScheduled(String format, String title, Interval interval, List<User> participants) {
        String message = String.format(
                format,
                title,
                interval.start(),
                interval.end()
        );
        notificationService.notifyUsers(participants, message);
    }
}
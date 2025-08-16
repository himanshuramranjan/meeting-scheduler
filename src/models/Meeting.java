package models;

import enums.MeetingStatus;
import service.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Meeting {
    private final String meetingId;
    private final String title;
    private final User organizer;
    private final List<User> participants;
    private final Interval interval;
    private final MeetingRoom room;
    private MeetingStatus status;
    private final Map<User, Boolean> rsvp;

    public Meeting(String title, User organizer, List<User> participants, Interval interval, MeetingRoom room) {
        this.meetingId = UUID.randomUUID().toString();
        this.title = title;
        this.organizer = organizer;
        this.participants = participants;
        this.interval = interval;
        this.room = room;
        this.status = MeetingStatus.SCHEDULED;
        this.rsvp = new HashMap<>();
        participants.forEach(p -> rsvp.put(p, null));
    }

    public Interval getInterval() { return interval; }

    public void updateRSVP(User user, boolean accept) {
        rsvp.put(user, accept);
    }

    public String getTitle() {
        return title;
    }

    public MeetingRoom getRoom() {
        return room;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void cancel() {
        this.status = MeetingStatus.CANCELED;

        String message = String.format(
                "Meeting canceled: %s | Originally scheduled: %s to %s",
                getTitle(),
                getInterval().start(),
                getInterval().end()
        );
        NotificationService.getInstance().notifyUsers(participants, message);
    }

    public void reschedule(Interval newInterval) {
        this.status = MeetingStatus.RESCHEDULED;

        String message = String.format(
                "Meeting rescheduled: %s | New Time: %s to %s",
                title,
                newInterval.start(),
                newInterval.end()
        );
        NotificationService.getInstance().notifyUsers(participants, message);
    }
}
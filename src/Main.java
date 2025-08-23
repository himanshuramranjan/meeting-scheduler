import models.Interval;
import models.Meeting;
import models.MeetingRoom;
import models.User;
import service.MeetingService;
import service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        User alice = new User("1", "Alice", "alice@example.com");
        User bob = new User("2", "Bob", "bob@example.com");

        MeetingRoom room = new MeetingRoom("101", "ConfRoom", 10);

        Interval slot = new Interval(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        MeetingService scheduler = MeetingService.getInstance();

        Meeting meeting = scheduler.scheduleMeeting("Project Kickoff", alice, Arrays.asList(alice, bob), slot, room);

        bob.respondToInvite(meeting, true);

        Interval newSlot = new Interval(LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(4));
        scheduler.rescheduleMeeting(meeting, newSlot);

        scheduler.cancelMeeting(meeting);
    }
}
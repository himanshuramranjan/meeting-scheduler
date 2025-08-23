package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calendar {
    private final User owner;
    private final List<Meeting> meetings;

    public Calendar(User owner) {
        this.owner = owner;
        this.meetings = new ArrayList<>();
    }

    public void addMeeting(Meeting meeting) {
        if (isConflict(meeting.getInterval())) {
            throw new RuntimeException("Conflict detected in calendar of " + owner.getName());
        }
        meetings.add(meeting);
    }

    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    public boolean isConflict(Interval interval) {
        return meetings.stream().anyMatch(m -> m.getInterval().overlapsWith(interval));
    }

    public List<Interval> isSlotAvailable(Interval requested) {
        if (!isConflict(requested)) return Collections.singletonList(requested);
        return Collections.emptyList();
    }
}
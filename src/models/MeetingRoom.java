package models;

import java.util.ArrayList;
import java.util.List;

public class MeetingRoom {
    private final String roomId;
    private final String name;
    private final int capacity;
    private final List<Interval> bookings;

    public MeetingRoom(String roomId, String name, int capacity) {
        this.roomId = roomId;
        this.name = name;
        this.capacity = capacity;
        this.bookings = new ArrayList<>();
    }

    public boolean checkAvailability(Interval interval) {
        return bookings.stream().noneMatch(b -> b.overlapsWith(interval));
    }

    public void book(Interval interval) {
        if (!checkAvailability(interval)) {
            throw new RuntimeException("Room already booked.");
        }
        bookings.add(interval);
    }
}
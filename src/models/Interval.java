package models;

import java.time.LocalDateTime;

public record Interval(LocalDateTime start, LocalDateTime end) {
    public Interval {
        if (start.isAfter(end)) throw new IllegalArgumentException("Start must be before end");
    }

    public boolean overlapsWith(Interval other) {
        return !(end.isBefore(other.start) || start.isAfter(other.end));
    }
}

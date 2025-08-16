package models;

public class User {
    private final String userId;
    private final String name;
    private final String email;
    private final Calendar calendar;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.calendar = new Calendar(this);
    }

    public Calendar getCalendar() { return calendar; }

    public void respondToInvite(Meeting meeting, boolean accept) {
        meeting.updateRSVP(this, accept);
    }

    public String getName() { return name; }
}
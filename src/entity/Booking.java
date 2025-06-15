package entity;

import java.util.Objects;

public class Booking {
    public enum EventType { THEATER, MUSIC }

    private final int id;
    private int customerId;
    private EventType eventType;
    private int eventId;

    public Booking(int id,int customerId,EventType type,int eventId){
        this.id=id; this.customerId=customerId; this.eventType=type; this.eventId=eventId;
    }

    public int getId(){ return id; }
    public int getCustomerId(){ return customerId; }
    public EventType getEventType(){ return eventType; }
    public int getEventId(){ return eventId; }

    @Override public String toString(){ return id + "|" + customerId + "|" + eventType + "|" + eventId; }
    public static Booking fromString(String s){
        var p=s.split("\\|");
        return new Booking(Integer.parseInt(p[0]),Integer.parseInt(p[1]),EventType.valueOf(p[2]),Integer.parseInt(p[3]));
    }
    @Override public boolean equals(Object o){ return o instanceof Booking b && b.id==id; }
    @Override public int hashCode(){ return Objects.hash(id); }
}
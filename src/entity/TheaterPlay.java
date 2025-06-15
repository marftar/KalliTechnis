package entity;

import java.time.LocalDate;
import java.util.Objects;

public class TheaterPlay {
    private final int id;
    private String title;
    private String protagonist;
    private String venue;
    private LocalDate date;

    public TheaterPlay(int id, String title, String protagonist, String venue, LocalDate date) {
        this.id = id;
        this.title = title;
        this.protagonist = protagonist;
        this.venue = venue;
        this.date = date;
    }

    public int getId()                 { return id; }
    public String getTitle()           { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getProtagonist()           { return protagonist; }
    public void setProtagonist(String protagonist) { this.protagonist = protagonist; }
    public String getVenue()           { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public LocalDate getDate()         { return date; }
    public void setDate(LocalDate date){ this.date = date; }

    @Override public String toString() { return id + "|" + title + "|" + protagonist + "|" + venue + "|" + date; }
    public static TheaterPlay fromString(String s){
        var p = s.split("\\|");
        return new TheaterPlay(Integer.parseInt(p[0]), p[1], p[2], p[3], LocalDate.parse(p[4]));
    }
    @Override public boolean equals(Object o){ return o instanceof TheaterPlay t && t.id==id; }
    @Override public int hashCode(){ return Objects.hash(id); }
}

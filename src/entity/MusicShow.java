package entity;

import java.time.LocalDate;
import java.util.Objects;

public class MusicShow {
    private final int id;
    private String title;
    private String singer;
    private String venue;
    private LocalDate date;

    public MusicShow(int id, String title, String singer, String venue, LocalDate date) {
        this.id = id;
        this.title = title;
        this.singer = singer;
        this.venue = venue;
        this.date = date;
    }

    public int getId()               { return id; }
    public String getTitle()         { return title; }
    public void setTitle(String t)   { title = t; }
    public String getSinger()        { return singer; }
    public void setSinger(String s)  { singer = s; }
    public String getVenue()         { return venue; }
    public void setVenue(String v)   { venue = v; }
    public LocalDate getDate()       { return date; }
    public void setDate(LocalDate d) { date = d; }

    @Override public String toString(){ return id + "|" + title + "|" + singer + "|" + venue + "|" + date; }
    public static MusicShow fromString(String s){
        var p = s.split("\\|");
        return new MusicShow(Integer.parseInt(p[0]), p[1], p[2], p[3], LocalDate.parse(p[4]));
    }
    @Override public boolean equals(Object o){ return o instanceof MusicShow m && m.id==id; }
    @Override public int hashCode(){ return Objects.hash(id); }
}
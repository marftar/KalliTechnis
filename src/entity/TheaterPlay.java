package entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Αντιπροσωπεύει μια θεατρική παράσταση με τα βασικά της χαρακτηριστικά:
 * κωδικό, τίτλο, πρωταγωνιστή, χώρο και ημερομηνία.
 */
public class TheaterPlay {
    private final int id;
    private String title;
    private String protagonist;
    private String venue;
    private LocalDate date;

    /**
     * Δημιουργεί ένα νέο αντικείμενο θεατρικής παράστασης.
     *
     * @param id Κωδικός της παράστασης
     * @param title Τίτλος της παράστασης
     * @param protagonist Πρωταγωνιστής της παράστασης
     * @param venue Χώρος διεξαγωγής
     * @param date Ημερομηνία παράστασης
     */
    public TheaterPlay(int id, String title, String protagonist, String venue, LocalDate date) {
        this.id = id;
        this.title = title;
        this.protagonist = protagonist;
        this.venue = venue;
        this.date = date;
    }

    /**
     * Επιστρέφει τον κωδικό της παράστασης.
     *
     * @return Κωδικός (int)
     */
    public int getId() {
        return id;
    }

    /**
     * Επιστρέφει τον τίτλο της παράστασης.
     *
     * @return Τίτλος
     */
    public String getTitle() {
        return title;
    }

    /**
     * Ορίζει νέο τίτλο στην παράσταση.
     *
     * @param title Νέος τίτλος
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Επιστρέφει το όνομα του πρωταγωνιστή.
     *
     * @return Πρωταγωνιστής
     */
    public String getProtagonist() {
        return protagonist;
    }

    /**
     * Ορίζει νέο πρωταγωνιστή στην παράσταση.
     *
     * @param protagonist Νέος πρωταγωνιστής
     */
    public void setProtagonist(String protagonist) {
        this.protagonist = protagonist;
    }

    /**
     * Επιστρέφει τον χώρο διεξαγωγής της παράστασης.
     *
     * @return Χώρος
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Ορίζει νέο χώρο διεξαγωγής στην παράσταση.
     *
     * @param venue Νέος χώρος
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Επιστρέφει την ημερομηνία της παράστασης.
     *
     * @return Ημερομηνία
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Ορίζει νέα ημερομηνία στην παράσταση.
     *
     * @param date Νέα ημερομηνία
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Επιστρέφει συμβολοσειρά με τα στοιχεία της παράστασης σε μορφή που μπορεί να αποθηκευτεί σε αρχείο.
     *
     * @return Συμβολοσειρά μορφής id|title|protagonist|venue|date
     */
    @Override
    public String toString() {
        return id + "|" + title + "|" + protagonist + "|" + venue + "|" + date;
    }

    /**
     * Δημιουργεί αντικείμενο TheaterPlay από συμβολοσειρά που έχει φορτωθεί από αρχείο.
     *
     * @param s Συμβολοσειρά μορφής id|title|protagonist|venue|date
     * @return Νέο αντικείμενο TheaterPlay
     */
    public static TheaterPlay fromString(String s) {
        var p = s.split("\\|");
        return new TheaterPlay(
                Integer.parseInt(p[0]),
                p[1],
                p[2],
                p[3],
                LocalDate.parse(p[4])
        );
    }

    /**
     * Ελέγχει αν δύο παραστάσεις έχουν τον ίδιο κωδικό.
     *
     * @param o Αντικείμενο προς σύγκριση
     * @return true αν έχουν τον ίδιο id, αλλιώς false
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof TheaterPlay t && t.id == id;
    }

    /**
     * Υπολογίζει το hash code με βάση τον κωδικό της παράστασης.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

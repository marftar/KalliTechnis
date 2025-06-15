package entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Η κλάση {@code MusicShow} αναπαριστά μια μουσική παράσταση
 * με βασικά χαρακτηριστικά όπως: κωδικός, τίτλος, τραγουδιστής,
 * χώρος και ημερομηνία.
 */
public class MusicShow {
    private final int id;
    private String title;
    private String singer;
    private String venue;
    private LocalDate date;

    /**
     * Κατασκευάζει μια νέα μουσική παράσταση.
     *
     * @param id Κωδικός της παράστασης
     * @param title Τίτλος της παράστασης
     * @param singer Τραγουδιστής
     * @param venue Χώρος διεξαγωγής
     * @param date Ημερομηνία παράστασης
     */
    public MusicShow(int id, String title, String singer, String venue, LocalDate date) {
        this.id = id;
        this.title = title;
        this.singer = singer;
        this.venue = venue;
        this.date = date;
    }

    /**
     * Επιστρέφει τον κωδικό της παράστασης.
     *
     * @return Κωδικός παράστασης
     */
    public int getId() {
        return id;
    }

    /**
     * Επιστρέφει τον τίτλο της μουσικής παράστασης.
     *
     * @return Τίτλος
     */
    public String getTitle() {
        return title;
    }

    /**
     * Ορίζει νέο τίτλο στη μουσική παράσταση.
     *
     * @param t Νέος τίτλος
     */
    public void setTitle(String t) {
        this.title = t;
    }

    /**
     * Επιστρέφει το όνομα του τραγουδιστή.
     *
     * @return Τραγουδιστής
     */
    public String getSinger() {
        return singer;
    }

    /**
     * Ορίζει νέο τραγουδιστή στη μουσική παράσταση.
     *
     * @param s Νέος τραγουδιστής
     */
    public void setSinger(String s) {
        this.singer = s;
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
     * @param v Νέος χώρος
     */
    public void setVenue(String v) {
        this.venue = v;
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
     * @param d Νέα ημερομηνία
     */
    public void setDate(LocalDate d) {
        this.date = d;
    }

    /**
     * Επιστρέφει συμβολοσειρά που περιγράφει την παράσταση, κατάλληλη για αποθήκευση σε αρχείο.
     *
     * @return Συμβολοσειρά μορφής {@code id|title|singer|venue|date}
     */
    @Override
    public String toString() {
        return id + "|" + title + "|" + singer + "|" + venue + "|" + date;
    }

    /**
     * Δημιουργεί αντικείμενο {@code MusicShow} από δεδομένα κειμένου.
     *
     * @param s Συμβολοσειρά μορφής {@code id|title|singer|venue|date}
     * @return Νέο αντικείμενο {@code MusicShow}
     */
    public static MusicShow fromString(String s) {
        var p = s.split("\\|");
        return new MusicShow(
                Integer.parseInt(p[0]),
                p[1],
                p[2],
                p[3],
                LocalDate.parse(p[4])
        );
    }

    /**
     * Ελέγχει αν δύο μουσικές παραστάσεις είναι ίδιες βάσει του κωδικού.
     *
     * @param o Αντικείμενο προς σύγκριση
     * @return {@code true} αν έχουν τον ίδιο {@code id}, αλλιώς {@code false}
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof MusicShow m && m.id == id;
    }

    /**
     * Υπολογίζει το hash code της παράστασης με βάση το {@code id}.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

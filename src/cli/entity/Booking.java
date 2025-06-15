package cli.entity;

import java.util.Objects;

/**
 * Η κλάση {@code Booking} αναπαριστά μία κράτηση εισιτηρίου από έναν πελάτη
 * για μια θεατρική ή μουσική παράσταση.
 */
public class Booking {

    /**
     * Τύπος της εκδήλωσης: θεατρική ή μουσική.
     */
    public enum EventType { THEATER, MUSIC }

    private final int id;
    private int customerId;
    private EventType eventType;
    private int eventId;

    /**
     * Δημιουργεί μια νέα κράτηση.
     *
     * @param id Κωδικός κράτησης
     * @param customerId Κωδικός πελάτη που κάνει την κράτηση
     * @param type Τύπος εκδήλωσης (THEATER ή MUSIC)
     * @param eventId Κωδικός της εκδήλωσης (θεατρικής ή μουσικής)
     */
    public Booking(int id, int customerId, EventType type, int eventId) {
        this.id = id;
        this.customerId = customerId;
        this.eventType = type;
        this.eventId = eventId;
    }

    /**
     * Επιστρέφει τον κωδικό της κράτησης.
     *
     * @return Κωδικός κράτησης
     */
    public int getId() {
        return id;
    }

    /**
     * Επιστρέφει τον κωδικό του πελάτη που έκανε την κράτηση.
     *
     * @return Κωδικός πελάτη
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Επιστρέφει τον τύπο της εκδήλωσης.
     *
     * @return {@code EventType} (THEATER ή MUSIC)
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Επιστρέφει τον κωδικό της εκδήλωσης για την οποία έγινε η κράτηση.
     *
     * @return Κωδικός εκδήλωσης
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Επιστρέφει τη συμβολοσειρά αναπαράστασης της κράτησης, κατάλληλη για αποθήκευση σε αρχείο.
     *
     * @return Συμβολοσειρά μορφής {@code id|customerId|eventType|eventId}
     */
    @Override
    public String toString() {
        return id + "|" + customerId + "|" + eventType + "|" + eventId;
    }

    /**
     * Δημιουργεί αντικείμενο {@code Booking} από συμβολοσειρά που έχει φορτωθεί από αρχείο.
     *
     * @param s Συμβολοσειρά μορφής {@code id|customerId|eventType|eventId}
     * @return Νέο αντικείμενο {@code Booking}
     */
    public static Booking fromString(String s) {
        var p = s.split("\\|");
        return new Booking(
                Integer.parseInt(p[0]),
                Integer.parseInt(p[1]),
                EventType.valueOf(p[2]),
                Integer.parseInt(p[3])
        );
    }

    /**
     * Ελέγχει αν δύο κρατήσεις είναι ίδιες βάσει του κωδικού τους.
     *
     * @param o Αντικείμενο προς σύγκριση
     * @return {@code true} αν οι κρατήσεις έχουν τον ίδιο {@code id}, αλλιώς {@code false}
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Booking b && b.id == id;
    }

    /**
     * Υπολογίζει το hash code της κράτησης βάσει του {@code id}.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

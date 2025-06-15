package cli.entity;

import java.util.Objects;

/**
 * Η κλάση {@code Customer} αναπαριστά έναν πελάτη του συστήματος,
 * ο οποίος έχει έναν μοναδικό κωδικό και ένα όνομα.
 */
public class Customer {
    private final int id;
    private String name;

    /**
     * Δημιουργεί έναν νέο πελάτη.
     *
     * @param id Κωδικός πελάτη
     * @param name Όνομα πελάτη
     */
    public Customer(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * Επιστρέφει τον κωδικό του πελάτη.
     *
     * @return Κωδικός (int)
     */
    public int getId() {
        return id;
    }

    /**
     * Επιστρέφει το όνομα του πελάτη.
     *
     * @return Όνομα
     */
    public String getName() {
        return name;
    }

    /**
     * Ορίζει νέο όνομα για τον πελάτη.
     *
     * @param n Νέο όνομα
     */
    public void setName(String n) {
        this.name = n;
    }

    /**
     * Επιστρέφει αναπαράσταση της κατάστασης του πελάτη σε μορφή συμβολοσειράς,
     * κατάλληλη για αποθήκευση σε αρχείο.
     *
     * @return Συμβολοσειρά μορφής {@code id|name}
     */
    @Override
    public String toString() {
        return id + "|" + name;
    }

    /**
     * Δημιουργεί αντικείμενο {@code Customer} από συμβολοσειρά.
     *
     * @param s Συμβολοσειρά μορφής {@code id|name}
     * @return Νέο αντικείμενο {@code Customer}
     */
    public static Customer fromString(String s) {
        var p = s.split("\\|");
        return new Customer(Integer.parseInt(p[0]), p[1]);
    }

    /**
     * Ελέγχει αν δύο πελάτες είναι ίσοι βάσει του κωδικού τους.
     *
     * @param o Αντικείμενο προς σύγκριση
     * @return {@code true} αν οι κωδικοί είναι ίσοι, αλλιώς {@code false}
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Customer c && c.id == id;
    }

    /**
     * Υπολογίζει το hash code του πελάτη βάσει του {@code id}.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

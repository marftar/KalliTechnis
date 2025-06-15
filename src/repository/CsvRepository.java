package repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

/**
 * Η κλάση {@code CsvRepository<T>} αποτελεί γενική υλοποίηση αποθετηρίου δεδομένων
 * σε αρχεία τύπου CSV, με δυνατότητα φόρτωσης, αποθήκευσης και βασικών λειτουργιών CRUD.
 *
 * @param <T> Ο τύπος των αντικειμένων που διαχειρίζεται το αποθετήριο
 */
public class CsvRepository<T> {

    private final Path path;
    private final Function<String, T> parser;
    private final Function<T, String> serializer;
    private final Map<Integer, T> data = new LinkedHashMap<>();

    /**
     * Δημιουργεί νέο αποθετήριο δεδομένων σε αρχείο CSV.
     *
     * @param file Όνομα ή διαδρομή του αρχείου CSV
     * @param parser Συνάρτηση που μετατρέπει μια γραμμή CSV σε αντικείμενο τύπου {@code T}
     * @param serializer Συνάρτηση που μετατρέπει αντικείμενο τύπου {@code T} σε γραμμή CSV
     */
    public CsvRepository(String file, Function<String, T> parser, Function<T, String> serializer) {
        this.path = Paths.get(file);
        this.parser = parser;
        this.serializer = serializer;
    }

    /**
     * Φορτώνει τα δεδομένα από το αρχείο CSV στη μνήμη.
     * Αν το αρχείο δεν υπάρχει, δεν γίνεται καμία ενέργεια.
     */
    public void load() {
        if (!Files.exists(path)) return;
        try (var lines = Files.lines(path)) {
            lines.filter(l -> !l.isBlank()).map(parser).forEach(t -> data.put(id(t), t));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Αποθηκεύει τα δεδομένα από τη μνήμη στο αρχείο CSV.
     */
    public void save() {
        try {
            Files.createDirectories(Optional.ofNullable(path.getParent()).orElse(Path.of(".")));
            Files.write(path, data.values().stream().map(serializer).toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Επιστρέφει όλα τα αποθηκευμένα αντικείμενα του αποθετηρίου.
     *
     * @return Συλλογή όλων των αντικειμένων
     */
    public Collection<T> findAll() {
        return data.values();
    }

    /**
     * Αναζητά ένα αντικείμενο βάσει του μοναδικού του κωδικού (ID).
     *
     * @param id Ο κωδικός του αντικειμένου
     * @return Προαιρετικό αντικείμενο {@code Optional<T>}
     */
    public Optional<T> findById(int id) {
        return Optional.ofNullable(data.get(id));
    }

    /**
     * Διαγράφει το αντικείμενο με τον δοθέντα κωδικό από το αποθετήριο.
     *
     * @param id Ο κωδικός του αντικειμένου προς διαγραφή
     */
    public void delete(int id) {
        data.remove(id);
    }

    /**
     * Αποθηκεύει ή ενημερώνει ένα αντικείμενο στο αποθετήριο βάσει του κωδικού του.
     *
     * @param t Το αντικείμενο προς αποθήκευση
     */
    public void save(T t) {
        data.put(id(t), t);
    }

    /**
     * Επιστρέφει τον επόμενο διαθέσιμο κωδικό (ID) για νέα εγγραφή.
     *
     * @return Ο επόμενος διαθέσιμος ακέραιος κωδικός
     */
    public int nextId() {
        return data.keySet().stream().mapToInt(i -> i).max().orElse(0) + 1;
    }

    /**
     * Βοηθητική μέθοδος που ανακτά το {@code getId()} από το αντικείμενο χρησιμοποιώντας reflection.
     *
     * @param t Το αντικείμενο
     * @return Η τιμή του {@code getId()}
     */
    private int id(T t) {
        try {
            return (int) t.getClass().getMethod("getId").invoke(t);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

import entity.Booking;
import entity.Customer;
import entity.MusicShow;
import entity.TheaterPlay;
import repository.CsvRepository;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * <p>Κύρια κλάση εκκίνησης της εφαρμογής κονσόλας. Παρέχει πολύχρωμο,
 * φιλικό προς τον χρήστη μενού και όλες τις βασικές ροές για
 * διαχείριση παραστάσεων, πελατών, κρατήσεων και στατιστικών.</p>
 *
 * <h2>Χρήση χρωμάτων</h2>
 * Τα ANSI escape codes χρησιμοποιούνται για εμπλουτισμένο UX:
 * <ul>
 *   <li><b>CYAN</b> για τίτλους/μενού</li>
 *   <li><b>YELLOW</b> για προτροπές (prompts)</li>
 *   <li><b>GREEN</b> για επιτυχείς ενέργειες</li>
 *   <li><b>RED</b> για σφάλματα</li>
 * </ul>
 *
 * <p>Οι βασικές λειτουργίες υλοποιούνται σε ιδιωτικές μεθόδους ώστε να
 * διατηρείται ο κώδικας <b>DRY</b> και <b>KISS</b>.</p>
 */
public class Main {

    /* ---------- ANSI Χρώματα ---------- */
    private static final String RESET  = "\u001B[0m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";

    /* ---------- Είσοδος Χρήστη ---------- */
    private static final Scanner IN = new Scanner(System.in);

    /* ---------- Αποθετήρια ---------- */
    private static final CsvRepository<TheaterPlay> theaterRepo =
            new CsvRepository<>("theater.csv", TheaterPlay::fromString, TheaterPlay::toString);
    private static final CsvRepository<MusicShow> musicRepo =
            new CsvRepository<>("music.csv", MusicShow::fromString, MusicShow::toString);
    private static final CsvRepository<Customer> customerRepo =
            new CsvRepository<>("customers.csv", Customer::fromString, Customer::toString);
    private static final CsvRepository<Booking> bookingRepo =
            new CsvRepository<>("bookings.csv", Booking::fromString, Booking::toString);

    /**
     * Σημείο εκκίνησης της εφαρμογής.
     *
     * @param args ορίσματα γραμμής εντολών (δεν χρησιμοποιούνται)
     */
    public static void main(String[] args) {
        load();
        loop();
        save();
        System.out.println(GREEN + "Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή!" + RESET);
    }

    /* ---------- Βοηθητικές βασικές λειτουργίες ---------- */

    /** Φορτώνει τα δεδομένα από τα αντίστοιχα CSV αρχεία. */
    private static void load() {
        theaterRepo.load();
        musicRepo.load();
        customerRepo.load();
        bookingRepo.load();
    }

    /** Αποθηκεύει τα δεδομένα πίσω στα CSV αρχεία. */
    private static void save() {
        theaterRepo.save();
        musicRepo.save();
        customerRepo.save();
        bookingRepo.save();
    }

    /* ---------- Κύριος βρόχος CLI ---------- */

    /** Επαναλαμβανόμενος βρόχος μενού μέχρι ο χρήστης να επιλέξει έξοδο. */
    private static void loop() {
        while (true) {
            printMenu();
            switch (intChoice()) {
                case 1  -> manageTheater();
                case 2  -> manageMusic();
                case 3  -> manageCustomers();
                case 4  -> book(Booking.EventType.THEATER);
                case 5  -> book(Booking.EventType.MUSIC);
                case 6  -> stats();
                case 0  -> { return; }
                default -> error("Άκυρη επιλογή");
            }
        }
    }

    /* ---------- Εκτυπώσεις UI ---------- */

    /** Εκτυπώνει το κεντρικό μενού με χρώματα. */
    private static void printMenu() {
        System.out.println(CYAN +
                "\n╔══════════════════════════════════════════╗\n" +
                "║         ΔΙΑΧΕΙΡΙΣΗ ΚΑΛΛΙΤΕΧΝΙΚΩΝ        ║\n" +
                "╠══════════════════════════════════════════╣\n" +
                "║ 1. Θεατρικές παραστάσεις                ║\n" +
                "║ 2. Μουσικές παραστάσεις                 ║\n" +
                "║ 3. Πελάτες                              ║\n" +
                "║ 4. Κράτηση θεατρικής παράστασης         ║\n" +
                "║ 5. Κράτηση μουσικής παράστασης          ║\n" +
                "║ 6. Στατιστικά εισιτηρίων                ║\n" +
                "║ 0. Έξοδος                               ║\n" +
                "╚══════════════════════════════════════════╝" + RESET);
        System.out.print(YELLOW + "➤ Επιλογή: " + RESET);
    }

    /* ---------- Διαχείριση οντοτήτων ---------- */

    private static void manageTheater() {
        crudMenu(
                () -> theaterRepo.save(new TheaterPlay(
                        theaterRepo.nextId(),
                        ask("Τίτλος"), ask("Πρωταγωνιστής"),
                        ask("Χώρος"), readDate())
                ),
                () -> editEntity(theaterRepo, "Κωδικός", tp -> {
                    tp.setTitle(optional(ask("Νέος τίτλος"), tp.getTitle()));
                    tp.setProtagonist(optional(ask("Νέος πρωταγωνιστής"), tp.getProtagonist()));
                    tp.setVenue(optional(ask("Νέος χώρος"), tp.getVenue()));
                    tp.setDate(optionalDate(readDateOptional(), tp.getDate()));
                }),
                () -> deleteById(theaterRepo, "Κωδικός"),
                () -> theaterRepo.findAll().forEach(System.out::println));
    }

    private static void manageMusic() {
        crudMenu(
                () -> musicRepo.save(new MusicShow(
                        musicRepo.nextId(),
                        ask("Τίτλος"), ask("Τραγουδιστής"),
                        ask("Χώρος"), readDate())
                ),
                () -> editEntity(musicRepo, "Κωδικός", ms -> {
                    ms.setTitle(optional(ask("Νέος τίτλος"), ms.getTitle()));
                    ms.setSinger(optional(ask("Νέος τραγουδιστής"), ms.getSinger()));
                    ms.setVenue(optional(ask("Νέος χώρος"), ms.getVenue()));
                    ms.setDate(optionalDate(readDateOptional(), ms.getDate()));
                }),
                () -> deleteById(musicRepo, "Κωδικός"),
                () -> musicRepo.findAll().forEach(System.out::println));
    }

    private static void manageCustomers() {
        crudMenu(
                () -> customerRepo.save(new Customer(customerRepo.nextId(), ask("Όνομα"))),
                () -> editEntity(customerRepo, "Κωδικός", c ->
                        c.setName(optional(ask("Νέο όνομα"), c.getName()))),
                () -> deleteById(customerRepo, "Κωδικός"),
                () -> customerRepo.findAll().forEach(System.out::println));
    }

    /* ---------- Κρατήσεις ---------- */

    private static void book(Booking.EventType type) {
        int custId = intInput("Κωδικός πελάτη");
        if (customerRepo.findById(custId).isEmpty()) {
            error("Δεν υπάρχει πελάτης");
            return;
        }

        if (type == Booking.EventType.THEATER) theaterRepo.findAll().forEach(System.out::println);
        else musicRepo.findAll().forEach(System.out::println);

        int evId = intInput("Κωδικός παράστασης");
        boolean ok = (type == Booking.EventType.THEATER)
                ? theaterRepo.findById(evId).isPresent()
                : musicRepo.findById(evId).isPresent();
        if (!ok) {
            error("Δεν υπάρχει παράσταση");
            return;
        }

        bookingRepo.save(new Booking(bookingRepo.nextId(), custId, type, evId));
        success("Η κράτηση ολοκληρώθηκε");
    }

    /* ---------- Στατιστικά ---------- */

    private static void stats() {
        var theaterStats = bookingRepo.findAll().stream()
                .filter(b -> b.getEventType() == Booking.EventType.THEATER)
                .collect(Collectors.groupingBy(Booking::getEventId, Collectors.counting()));
        var musicStats = bookingRepo.findAll().stream()
                .filter(b -> b.getEventType() == Booking.EventType.MUSIC)
                .collect(Collectors.groupingBy(Booking::getEventId, Collectors.counting()));

        System.out.println(CYAN + "\n╔═══ ΣΤΑΤΙΣΤΙΚΑ ═════════════════════╗" + RESET);
        System.out.println("Θεατρικές παραστάσεις:");
        theaterRepo.findAll().forEach(tp ->
                System.out.println("  • " + tp.getTitle() + ": "
                        + theaterStats.getOrDefault(tp.getId(), 0L)));
        System.out.println("Μουσικές παραστάσεις:");
        musicRepo.findAll().forEach(ms ->
                System.out.println("  • " + ms.getTitle() + ": "
                        + musicStats.getOrDefault(ms.getId(), 0L)));
    }

    /* ---------- CRUD Helper ---------- */

    private interface Editor<T> { void apply(T t); }

    private static <T> void editEntity(CsvRepository<T> repo, String prompt, Editor<T> ed) {
        int id = intInput(prompt);
        repo.findById(id).ifPresentOrElse(t -> {
            ed.apply(t);
            repo.save(t);
            success("Η ενημέρωση ολοκληρώθηκε");
        }, () -> error("Δεν βρέθηκε"));
    }

    private static void deleteById(CsvRepository<?> repo, String prompt) {
        repo.delete(intInput(prompt));
        success("Η διαγραφή ολοκληρώθηκε");
    }

    private static void crudMenu(Runnable add, Runnable edit, Runnable del, Runnable list) {
        System.out.println(CYAN +
                "\na. Εισαγωγή\nb. Διόρθωση\nc. Διαγραφή\nd. Λίστα" + RESET);
        System.out.print(YELLOW + "➤ Επιλογή: " + RESET);
        switch (IN.nextLine().trim().toLowerCase()) {
            case "a" -> add.run();
            case "b" -> edit.run();
            case "c" -> del.run();
            case "d" -> list.run();
            default  -> error("Άγνωστη εντολή");
        }
    }

    /* ---------- Input Helpers ---------- */

    private static int intChoice() {
        try { return Integer.parseInt(IN.nextLine().trim()); }
        catch (Exception e) { return -1; }
    }

    private static int intInput(String prompt) {
        return Integer.parseInt(ask(prompt));
    }

    private static String ask(String p) {
        System.out.print(YELLOW + p + ": " + RESET);
        return IN.nextLine().trim();
    }

    private static LocalDate readDate() {
        while (true) {
            String s = ask("Ημερομηνία (YYYY-MM-DD)");
            try { return LocalDate.parse(s); }
            catch (Exception e) { error("Μη έγκυρη ημερομηνία"); }
        }
    }

    private static LocalDate readDateOptional() {
        String s = IN.nextLine().trim();
        return s.isBlank() ? null : LocalDate.parse(s);
    }

    private static LocalDate optionalDate(LocalDate v, LocalDate def) { return v == null ? def : v; }
    private static String optional(String v, String def)             { return v.isBlank() ? def : v; }

    /* ---------- Console Feedback ---------- */

    private static void error(String msg)   { System.out.println(RED   + "✖ " + msg + RESET); }
    private static void success(String msg) { System.out.println(GREEN + "✔ " + msg + RESET); }
}

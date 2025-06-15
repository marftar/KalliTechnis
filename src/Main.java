import cli.entity.Booking;
import cli.entity.Customer;
import cli.entity.MusicShow;
import cli.entity.TheaterPlay;
import cli.repository.CsvRepository;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Κύρια κλάση της εφαρμογής.
 * <p>Παρέχει πολύχρωμο, «boxed» CLI με βελτιωμένο UX για όλα τα μενού.</p>
 */
public class Main {

    /* ---------- ANSI ---------- */
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String MAGENTA= "\u001B[35m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";

    /* ---------- I/O ---------- */
    private static final Scanner IN = new Scanner(System.in);

    /* ---------- Repos ---------- */
    private static final CsvRepository<TheaterPlay> theaterRepo =
            new CsvRepository<>("theater.csv", TheaterPlay::fromString, TheaterPlay::toString);
    private static final CsvRepository<MusicShow> musicRepo =
            new CsvRepository<>("music.csv", MusicShow::fromString, MusicShow::toString);
    private static final CsvRepository<Customer> customerRepo =
            new CsvRepository<>("customers.csv", Customer::fromString, Customer::toString);
    private static final CsvRepository<Booking> bookingRepo =
            new CsvRepository<>("bookings.csv", Booking::fromString, Booking::toString);

    /* ---------- Main ---------- */
    public static void main(String[] args) {
        load();
        loop();
        save();
        printlnBox(GREEN + "✔  Ευχαριστούμε που χρησιμοποιήσατε την εφαρμογή!  " + RESET);
    }

    /* ---------- Core ---------- */
    private static void load() { theaterRepo.load(); musicRepo.load(); customerRepo.load(); bookingRepo.load(); }
    private static void save() { theaterRepo.save(); musicRepo.save(); customerRepo.save(); bookingRepo.save(); }

    /* ---------- Main Loop ---------- */
    private static void loop() {
        while (true) {
            clear();
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

    /* ---------- UI Helpers ---------- */

    /** Καθαρίζει την οθόνη (ANSI). */
    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Εκτυπώνει το κεντρικό μενού σε «κουτί». */
    private static void printMenu() {
        box("ΔΙΑΧΕΙΡΙΣΗ ΚΑΛΛΙΤΕΧΝΙΚΩΝ",
                "1. Θεατρικές παραστάσεις",
                "2. Μουσικές παραστάσεις",
                "3. Πελάτες",
                "4. Κράτηση θεατρικής παράστασης",
                "5. Κράτηση μουσικής παράστασης",
                "6. Στατιστικά εισιτηρίων",
                "0. Έξοδος");
        System.out.print(YELLOW + "➤ Επιλογή: " + RESET);
    }

    /** Υπο-μενού CRUD σε κουτί. */
    private static void crudMenu(Runnable add, Runnable edit, Runnable del, Runnable list) {
        box("ΥΠΟ-ΜΕΝΟΥ",
                "a. Εισαγωγή",
                "b. Διόρθωση",
                "c. Διαγραφή",
                "d. Λίστα",
                "x. Επιστροφή");
        System.out.print(YELLOW + "➤ Επιλογή: " + RESET);
        switch (IN.nextLine().trim().toLowerCase()) {
            case "a" -> add.run();
            case "b" -> edit.run();
            case "c" -> del.run();
            case "d" -> { clear(); list.run(); pause(); }
            default  -> { /* back or unknown */ }
        }
    }

    /** Εκτυπώνει κουτί με τίτλο & γραμμές. */
    private static void box(String title, String... lines) {
        int width = Math.max(title.length(), java.util.Arrays.stream(lines).mapToInt(String::length).max().orElse(0)) + 4;
        String top    = "╔" + "═".repeat(width) + "╗";
        String divider= "╠" + "═".repeat(width) + "╣";
        String bottom = "╚" + "═".repeat(width) + "╝";
        System.out.println(CYAN + top + RESET);
        System.out.println(CYAN + "║" + center(title, width) + "║" + RESET);
        System.out.println(CYAN + divider + RESET);
        for (String l : lines) System.out.println("║ " + pad(l, width - 2) + "║");
        System.out.println(CYAN + bottom + RESET);
    }

    /** Κουτί ενός μόνο μηνύματος (π.χ. επιβεβαίωση). */
    private static void printlnBox(String msg) {
        int w = msg.length() + 2;
        System.out.println("╔" + "═".repeat(w) + "╗");
        System.out.println("║ " + msg + " ║");
        System.out.println("╚" + "═".repeat(w) + "╝");
    }

    private static String center(String txt, int w) { int pad=(w - txt.length())/2; return " ".repeat(pad)+txt+" ".repeat(w-txt.length()-pad);}
    private static String pad(String txt, int w)    { return txt + " ".repeat(Math.max(0, w - txt.length())); }
    private static void pause() { System.out.print(YELLOW + "\nΠάτησε Enter για συνέχεια..." + RESET); IN.nextLine(); clear(); }

    /* ---------- Managers ---------- */

    private static void manageTheater()   { crudMenu(() -> theaterRepo.save(new TheaterPlay(
                    theaterRepo.nextId(), ask("Τίτλος"), ask("Πρωταγωνιστής"), ask("Χώρος"), readDate())),
            () -> editEntity(theaterRepo,"Κωδικός", tp -> {
                tp.setTitle(optional(ask("Νέος τίτλος"),tp.getTitle()));
                tp.setProtagonist(optional(ask("Νέος πρωταγωνιστής"),tp.getProtagonist()));
                tp.setVenue(optional(ask("Νέος χώρος"),tp.getVenue()));
                tp.setDate(optionalDate(readDateOptional(),tp.getDate()));}),
            () -> deleteById(theaterRepo,"Κωδικός"),
            () -> theaterRepo.findAll().forEach(System.out::println)); }

    private static void manageMusic()     { crudMenu(() -> musicRepo.save(new MusicShow(
                    musicRepo.nextId(), ask("Τίτλος"), ask("Τραγουδιστής"), ask("Χώρος"), readDate())),
            () -> editEntity(musicRepo,"Κωδικός", ms -> {
                ms.setTitle(optional(ask("Νέος τίτλος"),ms.getTitle()));
                ms.setSinger(optional(ask("Νέος τραγουδιστής"),ms.getSinger()));
                ms.setVenue(optional(ask("Νέος χώρος"),ms.getVenue()));
                ms.setDate(optionalDate(readDateOptional(),ms.getDate()));}),
            () -> deleteById(musicRepo,"Κωδικός"),
            () -> musicRepo.findAll().forEach(System.out::println)); }

    private static void manageCustomers() { crudMenu(() -> customerRepo.save(new Customer(customerRepo.nextId(), ask("Όνομα"))),
            () -> editEntity(customerRepo,"Κωδικός", c -> c.setName(optional(ask("Νέο όνομα"),c.getName()))),
            () -> deleteById(customerRepo,"Κωδικός"),
            () -> customerRepo.findAll().forEach(System.out::println)); }

    /* ---------- Bookings ---------- */
    private static void book(Booking.EventType type) {
        int custId = intInput("Κωδικός πελάτη");
        if (customerRepo.findById(custId).isEmpty()) { error("Δεν υπάρχει πελάτης"); return; }

        clear();
        if (type == Booking.EventType.THEATER) theaterRepo.findAll().forEach(System.out::println);
        else musicRepo.findAll().forEach(System.out::println);

        int evId = intInput("Κωδικός παράστασης");
        boolean ok = (type == Booking.EventType.THEATER)
                ? theaterRepo.findById(evId).isPresent()
                : musicRepo.findById(evId).isPresent();
        if (!ok) { error("Δεν υπάρχει παράσταση"); return; }

        bookingRepo.save(new Booking(bookingRepo.nextId(), custId, type, evId));
        success("Η κράτηση ολοκληρώθηκε");
    }

    /* ---------- Stats ---------- */
    private static void stats() {
        clear();

        var theaterStats = bookingRepo.findAll().stream()
                .filter(b -> b.getEventType() == Booking.EventType.THEATER)
                .collect(Collectors.groupingBy(Booking::getEventId, Collectors.counting()));
        var musicStats = bookingRepo.findAll().stream()
                .filter(b -> b.getEventType() == Booking.EventType.MUSIC)
                .collect(Collectors.groupingBy(Booking::getEventId, Collectors.counting()));

        /* Δημιουργούμε πλήρη πίνακα γραμμών για κάθε κατηγορία */
        String[] theaterLines = java.util.stream.Stream.concat(
                java.util.stream.Stream.of("Θεατρικές παραστάσεις:"),
                theaterRepo.findAll().stream()
                        .map(tp -> "  • " + tp.getTitle() + ": " +
                                theaterStats.getOrDefault(tp.getId(), 0L))
        ).toArray(String[]::new);

        String[] musicLines = java.util.stream.Stream.concat(
                java.util.stream.Stream.of("Μουσικές παραστάσεις:"),
                musicRepo.findAll().stream()
                        .map(ms -> "  • " + ms.getTitle() + ": " +
                                musicStats.getOrDefault(ms.getId(), 0L))
        ).toArray(String[]::new);

        /* Εμφάνιση σε δύο κουτιά */
        box("ΣΤΑΤΙΣΤΙΚΑ", theaterLines);
        box(" ",           musicLines);   // αφήνουμε κενό τίτλο για να κρατήσουμε το ίδιο πλάτος
        pause();
    }


    /* ---------- Generic Helpers ---------- */
    @FunctionalInterface private interface Editor<T> { void apply(T t); }

    private static <T> void editEntity(CsvRepository<T> repo, String prompt, Editor<T> ed) {
        int id = intInput(prompt);
        repo.findById(id).ifPresentOrElse(t -> { ed.apply(t); repo.save(t); success("Η ενημέρωση ολοκληρώθηκε"); },
                () -> error("Δεν βρέθηκε"));
    }
    private static void deleteById(CsvRepository<?> repo,String prompt){ repo.delete(intInput(prompt)); success("Η διαγραφή ολοκληρώθηκε"); }

    private static int  intChoice(){ try{ return Integer.parseInt(IN.nextLine().trim()); } catch(Exception e){ return -1; } }
    private static int  intInput (String prompt){ return Integer.parseInt(ask(prompt)); }
    private static String ask(String p){ System.out.print(YELLOW + p + ": " + RESET); return IN.nextLine().trim(); }

    private static LocalDate readDate(){
        while(true){ String s=ask("Ημερομηνία (YYYY-MM-DD)");
            try{ return LocalDate.parse(s); } catch(Exception e){ error("Μη έγκυρη ημερομηνία"); } }
    }
    private static LocalDate readDateOptional(){ String s=IN.nextLine().trim(); return s.isBlank()?null:LocalDate.parse(s); }
    private static LocalDate optionalDate(LocalDate v,LocalDate def){ return v==null?def:v; }
    private static String     optional    (String v,String def){ return v.isBlank()?def:v; }

    /* ---------- Feedback ---------- */
    private static void error  (String msg){ printlnBox(RED   + "✖ " + msg + RESET); }
    private static void success(String msg){ printlnBox(GREEN + "✔ " + msg + RESET); }
}

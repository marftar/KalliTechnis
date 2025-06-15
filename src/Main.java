import entity.Booking;
import entity.Customer;
import entity.MusicShow;
import entity.TheaterPlay;
import repository.CsvRepository;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner IN=new Scanner(System.in);

    private static final CsvRepository<TheaterPlay> theaterRepo=
            new CsvRepository<>("theater.csv",TheaterPlay::fromString,TheaterPlay::toString);
    private static final CsvRepository<MusicShow> musicRepo=
            new CsvRepository<>("music.csv",MusicShow::fromString,MusicShow::toString);
    private static final CsvRepository<Customer> customerRepo=
            new CsvRepository<>("customers.csv",Customer::fromString,Customer::toString);
    private static final CsvRepository<Booking> bookingRepo=
            new CsvRepository<>("bookings.csv",Booking::fromString,Booking::toString);

    public static void main(String[] args){
        load(); loop(); save();
    }

    private static void load(){ theaterRepo.load(); musicRepo.load(); customerRepo.load(); bookingRepo.load(); }
    private static void save(){ theaterRepo.save(); musicRepo.save(); customerRepo.save(); bookingRepo.save(); }

    private static void loop(){
        while(true){
            System.out.println("""
                    1. Διαχείριση θεατρικών παραστάσεων
                    2. Διαχείριση μουσικών παραστάσεων
                    3. Διαχείριση πελατών
                    4. Κράτηση εισιτηρίου θεατρικής παράστασης
                    5. Κράτηση εισιτηρίου μουσικής παράστασης
                    6. Στατιστικά εισιτηρίων
                    0. Έξοδος
                    Επιλογή:""");
            switch(intChoice()){
                case 1 -> manageTheater();
                case 2 -> manageMusic();
                case 3 -> manageCustomers();
                case 4 -> book(Booking.EventType.THEATER);
                case 5 -> book(Booking.EventType.MUSIC);
                case 6 -> stats();
                case 0 -> { return; }
                default -> System.out.println("Άκυρη επιλογή");
            }
        }
    }

    /* ---------- entity management ---------- */

    private static void manageTheater(){ crudMenu(
            ()-> theaterRepo.save(new TheaterPlay(theaterRepo.nextId(),ask("Τίτλος"),ask("Πρωταγωνιστής"),ask("Χώρος"),readDate())),
            ()-> editEntity(theaterRepo,"Κωδικός",(tp)->{
                tp.setTitle(optional(ask("Νέος τίτλος"),tp.getTitle()));
                tp.setProtagonist(optional(ask("Νέος πρωταγωνιστής"),tp.getProtagonist()));
                tp.setVenue(optional(ask("Νέος χώρος"),tp.getVenue()));
                tp.setDate(optionalDate(readDateOptional(),tp.getDate()));
            }),
            ()-> deleteById(theaterRepo,"Κωδικός"),
            theaterRepo::findAll); }

    private static void manageMusic(){ crudMenu(
            ()-> musicRepo.save(new MusicShow(musicRepo.nextId(),ask("Τίτλος"),ask("Τραγουδιστής"),ask("Χώρος"),readDate())),
            ()-> editEntity(musicRepo,"Κωδικός",(ms)->{
                ms.setTitle(optional(ask("Νέος τίτλος"),ms.getTitle()));
                ms.setSinger(optional(ask("Νέος τραγουδιστής"),ms.getSinger()));
                ms.setVenue(optional(ask("Νέος χώρος"),ms.getVenue()));
                ms.setDate(optionalDate(readDateOptional(),ms.getDate()));
            }),
            ()-> deleteById(musicRepo,"Κωδικός"),
            musicRepo::findAll); }

    private static void manageCustomers(){ crudMenu(
            ()-> customerRepo.save(new Customer(customerRepo.nextId(),ask("Όνομα"))),
            ()-> editEntity(customerRepo,"Κωδικός",c-> c.setName(optional(ask("Νέο όνομα"),c.getName()))),
            ()-> deleteById(customerRepo,"Κωδικός"),
            customerRepo::findAll); }

    /* ---------- booking ---------- */

    private static void book(Booking.EventType type){
        int custId=intInput("Κωδικός πελάτη");
        if(customerRepo.findById(custId).isEmpty()){ System.out.println("Δεν υπάρχει πελάτης"); return; }

        if(type==Booking.EventType.THEATER) theaterRepo.findAll().forEach(System.out::println);
        else musicRepo.findAll().forEach(System.out::println);

        int evId=intInput("Κωδικός παράστασης");
        boolean ok= type==Booking.EventType.THEATER ?
                theaterRepo.findById(evId).isPresent() : musicRepo.findById(evId).isPresent();
        if(!ok){ System.out.println("Δεν υπάρχει παράσταση"); return; }

        bookingRepo.save(new Booking(bookingRepo.nextId(),custId,type,evId));
    }

    /* ---------- stats ---------- */

    private static void stats(){
        var theaterStats=bookingRepo.findAll().stream()
                .filter(b->b.getEventType()==Booking.EventType.THEATER)
                .collect(Collectors.groupingBy(Booking::getEventId,Collectors.counting()));
        var musicStats=bookingRepo.findAll().stream()
                .filter(b->b.getEventType()==Booking.EventType.MUSIC)
                .collect(Collectors.groupingBy(Booking::getEventId,Collectors.counting()));

        System.out.println("Θεατρικές παραστάσεις:");
        theaterRepo.findAll().forEach(tp-> System.out.println(tp.getTitle()+": "+theaterStats.getOrDefault(tp.getId(),0L)));
        System.out.println("Μουσικές παραστάσεις:");
        musicRepo.findAll().forEach(ms-> System.out.println(ms.getTitle()+": "+musicStats.getOrDefault(ms.getId(),0L)));
    }

    /* ---------- helpers ---------- */

    private interface Editor<T>{ void apply(T t); }

    private static <T> void editEntity(CsvRepository<T> repo,String prompt,Editor<T> ed){
        int id=intInput(prompt);
        repo.findById(id).ifPresentOrElse(t->{ ed.apply(t); repo.save(t); },()-> System.out.println("Δεν βρέθηκε"));
    }
    private static void deleteById(CsvRepository<?> repo,String prompt){ repo.delete(intInput(prompt)); }
    private static void crudMenu(Runnable add,Runnable edit,Runnable del,Runnable list){
        System.out.println("a. Εισαγωγή\nb. Διόρθωση\nc. Διαγραφή\nd. Λίστα\nΕπιλογή:");
        switch(IN.nextLine().trim()){
            case "a"-> add.run();
            case "b"-> edit.run();
            case "c"-> del.run();
            case "d"-> list.run();
        }
    }
    private static int intChoice(){ try{ return Integer.parseInt(IN.nextLine().trim()); }catch(Exception e){ return -1; } }
    private static int intInput(String prompt){ return Integer.parseInt(ask(prompt)); }
    private static String ask(String p){ System.out.print(p+": "); return IN.nextLine().trim(); }

    private static LocalDate readDate(){
        while(true){
            String s=ask("Ημερομηνία (YYYY-MM-DD)");
            try{ return LocalDate.parse(s); }catch(Exception e){ System.out.println("Μη έγκυρη ημερομηνία"); }
        }
    }
    private static LocalDate readDateOptional(){
        String s=IN.nextLine().trim();
        return s.isBlank()?null:LocalDate.parse(s);
    }
    private static LocalDate optionalDate(LocalDate v,LocalDate def){ return v==null?def:v; }
    private static String optional(String v,String def){ return v.isBlank()?def:v; }
}
import java.io.*;
import java.util.*;

public class UseCase1HotelBookingApp implements Serializable {

    private static Map<String, Integer> inventory = new HashMap<>();
    private static List<Reservation> confirmedReservations = new ArrayList<>();
    private static final String DATA_FILE = "bookingData.ser";

    public static void main(String[] args) {

        // Try to restore previous state
        if (!restoreState()) {
            // Initialize if no previous state exists
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
        }

        System.out.println("=== Current Inventory ===");
        inventory.forEach((room, count) -> System.out.println(room + " Available: " + count));

        // Simulate a booking
        Reservation res1 = new Reservation("Alice", "Single Room");
        if (processBooking(res1)) {
            System.out.println("Booking confirmed: " + res1.getGuestName() + " -> " + res1.getRoomType());
        }

        // Simulate another booking
        Reservation res2 = new Reservation("Bob", "Double Room");
        if (processBooking(res2)) {
            System.out.println("Booking confirmed: " + res2.getGuestName() + " -> " + res2.getRoomType());
        }

        // Display updated inventory
        System.out.println("\n=== Updated Inventory ===");
        inventory.forEach((room, count) -> System.out.println(room + " Available: " + count));

        // Save state before exit
        saveState();
    }

    // Booking logic
    private static boolean processBooking(Reservation res) {
        Integer available = inventory.get(res.getRoomType());
        if (available == null || available <= 0) {
            System.out.println("Booking failed for " + res.getGuestName() + ": " + res.getRoomType() + " not available.");
            return false;
        }
        inventory.put(res.getRoomType(), available - 1);
        confirmedReservations.add(res);
        return true;
    }

    // Save state to file
    private static void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(inventory);
            oos.writeObject(confirmedReservations);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Restore state from file
    private static boolean restoreState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return false;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            inventory = (Map<String, Integer>) ois.readObject();
            confirmedReservations = (List<Reservation>) ois.readObject();
            System.out.println("System state restored successfully.");
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring system state: " + e.getMessage());
            return false;
        }
    }
}

// Reservation class
class Reservation implements Serializable {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}
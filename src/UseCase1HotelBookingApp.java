import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;

public class UseCase1HotelBookingApp {

    public static void main(String[] args) {

        // ------------------------------
        // UC1: Room Inventory
        // ------------------------------
        HashMap<String, Integer> inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        System.out.println("=== UC1: Room Inventory ===");
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }

        // Update availability
        inventory.put("Double Room", 4);
        System.out.println("\n=== UC1: After Update ===");
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }

        // ------------------------------
        // UC4: Read-only Room Search
        // ------------------------------
        System.out.println("\n=== UC4: Available Rooms ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) { // Only show rooms with availability
                System.out.println(entry.getKey() + " Available: " + entry.getValue());
            }
        }

        // ------------------------------
        // UC5: Booking Request Queue (FIFO)
        // ------------------------------
        System.out.println("\n=== UC5: Booking Requests Queue ===");
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Guests submitting booking requests
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("Diana", "Single Room"));

        // Display queued requests
        for (Reservation request : bookingQueue) {
            System.out.println(request);
        }

        // Process requests in FIFO order (no inventory changes yet)
        System.out.println("\nProcessing Booking Requests (FIFO):");
        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            System.out.println("Processing " + request);
        }
    }
}

// Reservation class for UC5
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}
import java.util.HashMap;
import java.util.Map;

public class UseCase1HotelBookingApp {

    public static void main(String[] args) {

        // initialize room availability (same as UC1)
        HashMap<String, Integer> inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        // Display inventory (UC1)
        System.out.println("Room Inventory:\n");
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }

        // Update availability (UC1)
        inventory.put("Double Room", 4);
        System.out.println("\nAfter Update:\n");
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
    }
}
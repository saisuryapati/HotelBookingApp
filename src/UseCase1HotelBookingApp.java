import java.util.HashMap;

public class UseCase1HotelBookingApp {

    public static void main(String[] args) {

        HashMap<String, Integer> inventory = new HashMap<>();

        // initialize room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        System.out.println("Room Inventory:\n");

        // display inventory
        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }

        // update availability
        inventory.put("Double Room", 4);

        System.out.println("\nAfter Update:\n");

        for (String room : inventory.keySet()) {
            System.out.println(room + " Available: " + inventory.get(room));
        }
    }
}
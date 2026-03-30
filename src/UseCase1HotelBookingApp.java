import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

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
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey() + " Available: " + entry.getValue());
            }
        }

        // ------------------------------
        // UC5: Booking Request Queue (FIFO)
        // ------------------------------
        System.out.println("\n=== UC5: Booking Requests Queue ===");
        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("Diana", "Single Room"));
        bookingQueue.add(new Reservation("Eve", "Single Room"));

        for (Reservation request : bookingQueue) {
            System.out.println(request);
        }

        // ------------------------------
        // UC6: Reservation Confirmation & Room Allocation
        // ------------------------------
        System.out.println("\n=== UC6: Reservation Confirmation & Room Allocation ===");
        HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
        for (String roomType : inventory.keySet()) {
            allocatedRooms.put(roomType, new HashSet<>());
        }

        // Store confirmed reservations for UC7
        Map<String, Reservation> confirmedReservations = new HashMap<>();

        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            String roomType = request.getRoomType();
            int available = inventory.getOrDefault(roomType, 0);

            if (available > 0) {
                // Generate unique room ID
                String roomID = roomType.substring(0, 2).toUpperCase() + (allocatedRooms.get(roomType).size() + 1);
                allocatedRooms.get(roomType).add(roomID);

                // Update inventory
                inventory.put(roomType, available - 1);

                // Save confirmed reservation with room ID
                request.setRoomID(roomID);
                confirmedReservations.put(roomID, request);

                System.out.println(request.getGuestName() + " booked " + roomType + " with Room ID: " + roomID);
            } else {
                System.out.println("Sorry " + request.getGuestName() + ", " + roomType + " is fully booked.");
            }
        }

        System.out.println("\n=== Remaining Inventory ===");
        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " Available: " + inventory.get(roomType));
        }

        System.out.println("\n=== Allocated Rooms ===");
        for (String roomType : allocatedRooms.keySet()) {
            System.out.println(roomType + " Allocated IDs: " + allocatedRooms.get(roomType));
        }

        // ------------------------------
        // UC7: Add-On Service Selection
        // ------------------------------
        System.out.println("\n=== UC7: Add-On Service Selection ===");
        Map<String, List<Service>> reservationServices = new HashMap<>();

        // Sample services
        Service breakfast = new Service("Breakfast", 10);
        Service airportPickup = new Service("Airport Pickup", 20);
        Service spa = new Service("Spa Access", 30);

        // Guests select services
        addServiceToReservation(reservationServices, confirmedReservations.get("SI1"), breakfast, spa);
        addServiceToReservation(reservationServices, confirmedReservations.get("DO1"), airportPickup);

        // Display add-on services and total additional cost
        for (String roomID : reservationServices.keySet()) {
            List<Service> services = reservationServices.get(roomID);
            int totalCost = services.stream().mapToInt(Service::getCost).sum();
            System.out.println("Reservation " + roomID + " Services: " + services + " | Total Cost: $" + totalCost);
        }
    }

    private static void addServiceToReservation(Map<String, List<Service>> reservationServices, Reservation reservation, Service... services) {
        if (reservation == null) return; // skip if reservation not found
        reservationServices.putIfAbsent(reservation.getRoomID(), new ArrayList<>());
        for (Service s : services) {
            reservationServices.get(reservation.getRoomID()).add(s);
        }
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private String roomID;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomID() { return roomID; }
    public void setRoomID(String roomID) { this.roomID = roomID; }

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

// Service class for UC7
class Service {
    private String name;
    private int cost;

    public Service(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public int getCost() { return cost; }

    @Override
    public String toString() { return name; }
}
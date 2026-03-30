import java.util.*;

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
        inventory.forEach((room, count) -> System.out.println(room + " Available: " + count));

        // ------------------------------
        // UC4: Read-only Room Search
        // ------------------------------
        System.out.println("\n=== UC4: Available Rooms ===");
        inventory.forEach((room, count) -> {
            if (count > 0) System.out.println(room + " Available: " + count);
        });

        // ------------------------------
        // UC5: Booking Request Queue
        // ------------------------------
        Queue<Reservation> bookingQueue = new LinkedList<>();
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("Diana", "Single Room"));
        bookingQueue.add(new Reservation("Eve", "InvalidRoom")); // invalid for UC9 demo

        // ------------------------------
        // UC6: Reservation Confirmation & Room Allocation
        // ------------------------------
        HashMap<String, Set<String>> allocatedRooms = new HashMap<>();
        inventory.keySet().forEach(roomType -> allocatedRooms.put(roomType, new HashSet<>()));

        Map<String, Reservation> confirmedReservations = new HashMap<>();
        List<Reservation> bookingHistory = new ArrayList<>();

        System.out.println("\n=== UC9: Booking with Validation & Error Handling ===");
        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            try {
                validateBooking(request, inventory); // UC9 validation

                int available = inventory.get(request.getRoomType());
                String roomID = request.getRoomType().substring(0, 2).toUpperCase() + (allocatedRooms.get(request.getRoomType()).size() + 1);

                allocatedRooms.get(request.getRoomType()).add(roomID);
                inventory.put(request.getRoomType(), available - 1);

                request.setRoomID(roomID);
                confirmedReservations.put(roomID, request);
                bookingHistory.add(request);

                System.out.println(request.getGuestName() + " booked " + request.getRoomType() + " with Room ID: " + roomID);
            } catch (InvalidBookingException e) {
                System.out.println("Booking failed for " + request.getGuestName() + ": " + e.getMessage());
            }
        }

        // ------------------------------
        // UC8: Booking History Report
        // ------------------------------
        System.out.println("\n=== UC8: Booking History Report ===");
        for (Reservation r : bookingHistory) {
            System.out.println("Reservation ID: " + r.getRoomID() + ", Guest: " + r.getGuestName() + ", Room Type: " + r.getRoomType());
        }
        System.out.println("Total bookings confirmed: " + bookingHistory.size());
    }

    // ------------------------------
    // UC9: Validation Method
    // ------------------------------
    private static void validateBooking(Reservation reservation, Map<String, Integer> inventory) throws InvalidBookingException {
        if (!inventory.containsKey(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }
        if (inventory.get(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException(reservation.getRoomType() + " is fully booked");
        }
        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }
    }
}

// ------------------------------
// Reservation class
// ------------------------------
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

// ------------------------------
// UC9: Custom Exception
// ------------------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
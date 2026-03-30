import java.util.*;
import java.util.concurrent.*;

public class UseCase1HotelBookingApp {

    public static void main(String[] args) throws InterruptedException {

        // ------------------------------
        // UC1: Room Inventory
        // ------------------------------
        Map<String, Integer> inventory = new ConcurrentHashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);

        System.out.println("=== UC1: Room Inventory ===");
        inventory.forEach((room, count) -> System.out.println(room + " Available: " + count));

        // ------------------------------
        // UC11: Concurrent Booking Simulation
        // ------------------------------
        Queue<Reservation> bookingQueue = new ConcurrentLinkedQueue<>();
        bookingQueue.add(new Reservation("Alice", "Single Room"));
        bookingQueue.add(new Reservation("Bob", "Double Room"));
        bookingQueue.add(new Reservation("Charlie", "Suite Room"));
        bookingQueue.add(new Reservation("Diana", "Single Room"));
        bookingQueue.add(new Reservation("Eve", "Double Room"));

        Map<String, Set<String>> allocatedRooms = new ConcurrentHashMap<>();
        inventory.keySet().forEach(roomType -> allocatedRooms.put(roomType, ConcurrentHashMap.newKeySet()));

        Map<String, Reservation> confirmedReservations = new ConcurrentHashMap<>();

        System.out.println("\n=== UC11: Concurrent Booking Simulation ===");

        // Use a thread pool to simulate multiple guests booking at the same time
        ExecutorService executor = Executors.newFixedThreadPool(3);

        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            if (request != null) {
                executor.submit(() -> {
                    try {
                        synchronized (inventory) {
                            validateBooking(request, inventory);
                            int available = inventory.get(request.getRoomType());
                            String roomID = request.getRoomType().substring(0, 2).toUpperCase() + (allocatedRooms.get(request.getRoomType()).size() + 1);

                            allocatedRooms.get(request.getRoomType()).add(roomID);
                            inventory.put(request.getRoomType(), available - 1);

                            request.setRoomID(roomID);
                            confirmedReservations.put(roomID, request);

                            System.out.println(Thread.currentThread().getName() + " booked " + request.getRoomType() + " for " + request.getGuestName() + " -> Room ID: " + roomID);
                        }
                    } catch (InvalidBookingException e) {
                        System.out.println(Thread.currentThread().getName() + " failed for " + request.getGuestName() + ": " + e.getMessage());
                    }
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\n=== Final Inventory ===");
        inventory.forEach((room, count) -> System.out.println(room + " Available: " + count));

        System.out.println("\n=== Confirmed Reservations ===");
        confirmedReservations.forEach((id, r) -> System.out.println("Room ID: " + id + ", Guest: " + r.getGuestName() + ", Room Type: " + r.getRoomType()));
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
}

// ------------------------------
// UC9: Custom Exception
// ------------------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
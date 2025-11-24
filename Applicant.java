import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Applicant {

    private String applicantID;
    private String preferredRoomID;
    private String applicationStatus;
    private LinkedList<ViewingRequest> viewingRequests;

    private LinkedList<Room> roomList;
    private Scanner input = new Scanner(System.in);

    public Applicant(String applicantID) {
        this.applicantID = applicantID;
        this.preferredRoomID = "";
        this.applicationStatus = "None";
        this.viewingRequests = new LinkedList<>();

        roomList = new LinkedList<>();
        roomList.add(new Room("R101", "Single Room", 3500, "WiFi, Aircon, Bed, Cabinet", "Available"));
        roomList.add(new Room("R204", "Double Sharing", 2200, "WiFi, Fan, Table, Common CR", "2 Slots Left"));
    }

    public void viewAvailableRooms() {

        System.out.println("\n---- VIEW AVAILABLE ROOMS ----");

        // Show unsorted rooms first
        System.out.println("\n--- UNSORTED ROOMS ---");
        displayRooms(roomList);

        System.out.print("\nSort order (asc/desc): ");
        String order = input.nextLine().trim().toLowerCase();
        if (!order.equals("asc") && !order.equals("desc")) {
            System.out.println("Invalid order. Defaulting to ascending.");
            order = "asc";
        }

        LinkedList<Room> sorted = new LinkedList<>(roomList);

        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = 0; j < sorted.size() - i - 1; j++) {

                double priceA = sorted.get(j).getPrice();
                double priceB = sorted.get(j + 1).getPrice();

                boolean swap = false;
                if (order.equals("asc") && priceA > priceB) swap = true;
                if (order.equals("desc") && priceA < priceB) swap = true;

                if (swap) {
                    Room temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        System.out.println("\n--- SORTED ROOMS (BY PRICE - " + order.toUpperCase() + ") ---");
        displayRooms(sorted);
    }

    public void displayRooms(LinkedList<Room> list) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "PH")); // ₱ format
        nf.setMaximumFractionDigits(0);

        int num = 1;
        for (Room r : list) {
            System.out.println("\n" + (num++) + ". Room ID: " + r.getRoomID());
            System.out.println("   Type: " + r.getType());
            System.out.println("   Price: " + nf.format(r.getPrice()) + " / month");
            System.out.println("   Amenities: " + r.getAmenities());
            System.out.println("   Availability: " + r.getAvailability());
        }
    }

    public void applyForRoom() {
        System.out.print("Enter Room ID to apply: ");
        String roomID = input.nextLine();

        if (roomID.isEmpty()) {
            System.out.println("Invalid room ID.");
            return;
        }
        this.preferredRoomID = roomID;
        this.applicationStatus = "Pending";
        System.out.println("Application submitted for room " + roomID + ". Status: Pending");
    }

    public void checkApplicationStatus() {
        System.out.println("Current application status: " + this.applicationStatus);
    }

    public void scheduleViewing() {
        System.out.print("Enter Viewing Request ID: ");
        String requestID = input.nextLine();

        System.out.print("Enter Room ID: ");
        String rID = input.nextLine();

        System.out.print("Enter Viewing Date (YYYY-MM-DD): ");
        String date = input.nextLine();

        System.out.print("Enter Viewing Time (HH:MM): ");
        String time = input.nextLine();

        ViewingRequest vr = new ViewingRequest(requestID, rID, date, time);
        viewingRequests.add(vr);

        System.out.println("Viewing request scheduled: " + vr.getRequestID());
    }

    public void cancelOrRescheduleViewing() {
        System.out.print("Enter Viewing Request ID to cancel/reschedule: ");
        String cancelID = input.nextLine();

        System.out.println("1. Cancel");
        System.out.println("2. Reschedule");
        System.out.print("Choose option: ");

        int opt;

        try {
            opt = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid option!");
            return;
        }

        switch (opt) {
            case 1: cancelViewing(cancelID); break;

            case 2:
                System.out.print("Enter new Date (YYYY-MM-DD): ");
                String newDate = input.nextLine();

                System.out.print("Enter new Time (HH:MM): ");
                String newTime = input.nextLine();

                boolean found = false;
                for (ViewingRequest vreq : viewingRequests) {
                    if (vreq.getRequestID().equals(cancelID)) {
                        vreq.setDate(newDate);
                        vreq.setTime(newTime);
                        System.out.println("Viewing request rescheduled!");
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println("Request ID not found.");
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    public void cancelViewing(String requestID) {
        ViewingRequest toRemove = null;

        for (ViewingRequest vr : viewingRequests) {
            if (vr.getRequestID().equals(requestID)) {
                toRemove = vr;
                break;
            }
        }

        if (toRemove != null) {
            viewingRequests.remove(toRemove);
            System.out.println("Viewing request " + requestID + " has been cancelled.");
        } else {
            System.out.println("No viewing request found with ID: " + requestID);
        }
    }

    public void contactAdmin() {
        System.out.print("Enter message to admin: ");
        String msg = input.nextLine();

        if (msg.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return;
        }
        System.out.println("Message sent to admin: " + msg);
    }

    public void displayApplicantMenu() {
        System.out.println("\n===== APPLICANT MENU =====");
        System.out.println("1. View Available Rooms");
        System.out.println("2. Apply / Reserve Room");
        System.out.println("3. Check Application Status");
        System.out.println("4. Schedule Room Viewing");
        System.out.println("5. Cancel / Reschedule Viewing");
        System.out.println("6. Contact Admin");
        System.out.println("7. FAQ / Dorm Rules");
        System.out.println("0. Exit");
    }

    public void handleApplicantChoice(int choice) {
        switch (choice) {
            case 1: viewAvailableRooms(); break;
            case 2: applyForRoom(); break;
            case 3: checkApplicationStatus(); break;
            case 4: scheduleViewing(); break;
            case 5: cancelOrRescheduleViewing(); break;
            case 6: contactAdmin(); break;

            case 7:
                System.out.println("1. Curfew: 10:00 PM");
                System.out.println("2. No loud noise after 9 PM");
                System.out.println("3. Maintain cleanliness");
                System.out.println("4. Visitors must register at the lobby");
                System.out.println("5. No illegal appliances");
                break;

            case 0: System.out.println("Exiting menu..."); break;

            default: System.out.println("Invalid choice!");
        }
    }
}

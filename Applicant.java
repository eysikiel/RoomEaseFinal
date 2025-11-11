import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Applicant {
    private String applicantID;
    private String preferredRoomID;
    private String applicationStatus;
    private List<Viewing> scheduledViewings;
    private Scanner input;
    private int viewingCounter = 1;
    private int choice;

    private class Viewing {
        String requestID;
        String roomID;
        String date;
        String time;

        Viewing(String roomID, String date, String time) {
            this.requestID = "VR" + viewingCounter++;
            this.roomID = roomID;
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            return "RequestID: " + requestID + ", Room: " + roomID + ", Date: " + date + ", Time: " + time;
        }
    }

    public Applicant(String applicantID) {
        this.applicantID = applicantID;
        this.preferredRoomID = null;
        this.applicationStatus = "Not Applied";
        this.scheduledViewings = new ArrayList<>();
        this.input = new Scanner(System.in);
    }

    public void displayApplicantMenu() {
        do {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("               APPLICANT MAIN MENU             ");
            System.out.println("───────────────────────────────────────────────");
            System.out.println("[1] Apply for a Room");
            System.out.println("[2] Check Application Status");
            System.out.println("[3] Schedule a Viewing");
            System.out.println("[4] Cancel a Viewing");
            System.out.println("[5] List Scheduled Viewings");
            System.out.println("[6] Contact Admin");
            System.out.println("[7] Sort Available Rooms");
            System.out.println("[0] Exit");
            System.out.println("───────────────────────────────────────────────");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();
                input.nextLine();

                if (choice < 0 || choice > 7) {
                    System.out.println("Invalid input. Please enter a number between 0 and 7.\n");
                    continue;
                }
                handleApplicantChoice(choice);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number only.");
                input.nextLine();
            }

        } while (choice != 0);
    }

    private void handleApplicantChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.print("Enter Room ID to apply (or 'b' to go back): ");
                String roomID = input.nextLine();
                if (!roomID.equalsIgnoreCase("b")) applyForRoom(roomID);
                break;

            case 2:
                checkApplicationStatus();
                break;

            case 3:
                System.out.print("Enter Room ID for viewing (or 'b' to go back): ");
                String vRoom = input.nextLine();
                if (vRoom.equalsIgnoreCase("b")) break;

                System.out.print("Enter Date (YYYY-MM-DD): ");
                String date = input.nextLine();
                if (date.equalsIgnoreCase("b")) break;

                System.out.print("Enter Time (HH:MM): ");
                String time = input.nextLine();
                if (time.equalsIgnoreCase("b")) break;

                scheduleViewing(vRoom, date, time);
                break;

            case 4:
                System.out.print("Enter Viewing Request ID to cancel (or 'b'): ");
                String reqID = input.nextLine();
                if (!reqID.equalsIgnoreCase("b")) cancelViewing(reqID);
                break;

            case 5:
                listViewings();
                break;

            case 6:
                System.out.print("Enter message to admin (or 'b'): ");
                String msg = input.nextLine();
                if (!msg.equalsIgnoreCase("b")) contactAdmin(msg);
                break;

            case 7:
                System.out.print("Enter sort criteria (price/type/etc.) or 'b': ");
                String criteria = input.nextLine();
                if (criteria.equalsIgnoreCase("b")) break;

                System.out.print("Enter order (asc/desc) or 'b': ");
                String order = input.nextLine();
                if (order.equalsIgnoreCase("b")) break;

                sortAvailableRooms(criteria, order);
                break;

            case 0:
                System.out.println("Exiting Applicant Menu... Goodbye!");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void applyForRoom(String roomID) {
        this.preferredRoomID = roomID;
        this.applicationStatus = "Application Submitted";
        System.out.println("Applied for Room " + roomID);
    }

    private void checkApplicationStatus() {
        System.out.println("Application Status: " + applicationStatus);
    }

    private void scheduleViewing(String roomID, String date, String time) {
        Viewing v = new Viewing(roomID, date, time);
        scheduledViewings.add(v);
        System.out.println("Viewing scheduled: " + v);
    }

    private void cancelViewing(String requestID) {
        Viewing toRemove = null;
        for (Viewing v : scheduledViewings) {
            if (v.requestID.equals(requestID)) {
                toRemove = v;
                break;
            }
        }
        if (toRemove != null) {
            scheduledViewings.remove(toRemove);
            System.out.println("Viewing canceled: " + requestID);
        } else {
            System.out.println("No viewing found with ID: " + requestID);
        }
    }

    private void listViewings() {
        if (scheduledViewings.isEmpty()) {
            System.out.println("No scheduled viewings.");
        } else {
            System.out.println("Scheduled Viewings:");
            for (Viewing v : scheduledViewings) {
                System.out.println(v);
            }
        }
    }

    private void contactAdmin(String message) {
        System.out.println("Message sent to admin: " + message);
    }

    private void sortAvailableRooms(String criteria, String order) {
        System.out.println("Sorting rooms by " + criteria + " in " + order + " order");
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Applicant {
    private String applicantID;
    private String preferredRoomID;
    private String applicationStatus;
    private List<ViewingRequest> viewingRequests;

    public Applicant(String applicantID) {
        this.applicantID = applicantID;
        this.preferredRoomID = "";
        this.applicationStatus = "None";
        this.viewingRequests = new ArrayList<>();
    }

    public String getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public String getPreferredRoomID() {
        return preferredRoomID;
    }

    public void setPreferredRoomID(String preferredRoomID) {
        this.preferredRoomID = preferredRoomID;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public List<ViewingRequest> getViewingRequests() {
        return viewingRequests;
    }

    public void setViewingRequests(List<ViewingRequest> viewingRequests) {
        this.viewingRequests = viewingRequests;
    }

    public void applyForRoom(String roomID) {
        if (roomID == null || roomID.isEmpty()) {
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

    public void scheduleViewing(ViewingRequest request) {
        if (request == null) {
            System.out.println("Invalid viewing request.");
            return;
        }
        viewingRequests.add(request);
        System.out.println("Viewing request scheduled: " + request.getRequestID());
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

    public void contactAdmin(String message) {
        if (message == null || message.isEmpty()) {
            System.out.println("Message cannot be empty.");
            return;
        }
        System.out.println("Message sent to admin: " + message);
    }

    public void sortAvailableRooms(String criteria, String order) {
        System.out.println("Sorting rooms by " + criteria + " in " + order + " order...");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your Applicant ID: ");
        String applicantID = input.nextLine();

        Applicant applicant = new Applicant(applicantID);
        int choice;

        do {
            System.out.println("\n===== APPLICANT MENU =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Apply / Reserve Room");
            System.out.println("3. Check Application Status");
            System.out.println("4. Schedule Room Viewing");
            System.out.println("5. Cancel / Reschedule Viewing");
            System.out.println("6. Contact Admin");
            System.out.println("7. FAQ / Dorm Rules");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter filter criteria (price/type/amenities/availability): ");
                    String criteria = input.nextLine();
                    System.out.print("Enter sorting order (asc/desc): ");
                    String order = input.nextLine();
                    applicant.sortAvailableRooms(criteria, order);
                    break;

                case 2:
                    System.out.print("Enter Room ID to apply: ");
                    String roomID = input.nextLine();
                    applicant.applyForRoom(roomID);
                    break;

                case 3:
                    applicant.checkApplicationStatus();
                    break;

                case 4:
                    System.out.print("Enter Viewing Request ID: ");
                    String requestID = input.nextLine();
                    System.out.print("Enter Room ID: ");
                    String rID = input.nextLine();
                    System.out.print("Enter Viewing Date (YYYY-MM-DD): ");
                    String date = input.nextLine();
                    System.out.print("Enter Viewing Time (HH:MM): ");
                    String time = input.nextLine();
                    ViewingRequest vr = new ViewingRequest();
                    applicant.scheduleViewing(vr);
                    break;

                case 5:
                    System.out.print("Enter Viewing Request ID to cancel/reschedule: ");
                    String cancelID = input.nextLine();
                    System.out.println("1. Cancel");
                    System.out.println("2. Reschedule");
                    System.out.print("Choose option: ");
                    int opt = input.nextInt();
                    input.nextLine();

                    if (opt == 1) {
                        applicant.cancelViewing(cancelID);
                    } else if (opt == 2) {
                        System.out.print("Enter new Date (YYYY-MM-DD): ");
                        String newDate = input.nextLine();
                        System.out.print("Enter new Time (HH:MM): ");
                        String newTime = input.nextLine();
                        boolean found = false;

                        for (ViewingRequest vreq : applicant.getViewingRequests()) {
                            if (vreq.getRequestID().equals(cancelID)) {
                                vreq.setDate(newDate);
                                vreq.se ntTime(newTime);
                                System.out.println("Viewing request rescheduled!");
                                found = true;
                                break;
                            }
                        }

                        if (!found) System.out.println("Request ID not found.");
                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;

                case 6:
                    System.out.print("Enter message to admin: ");
                    String msg = input.nextLine();
                    applicant.contactAdmin(msg);
                    break;

                case 7:
                    System.out.println("1. Curfew: 10:00 PM");
                    System.out.println("2. No loud noise after 9 PM");
                    System.out.println("3. Maintain cleanliness");
                    System.out.println("4. Visitors must register at the lobby");
                    System.out.println("5. No illegal appliances");
                    break;

                case 0:
                    System.out.println("Exiting menu...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 0);

        input.close();
    }
}

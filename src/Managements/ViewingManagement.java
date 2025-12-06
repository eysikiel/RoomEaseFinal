package Managements;

// Use fully-qualified Model.Request.ViewingRequest to avoid name collision with Managements.ViewingRequest
import Model.User.Applicant;
import Model.Property.Room;
import Enums.RequestStatus;
import Enums.RoomStatus;
import Utilities.InputValidator;
import Database.DatabaseManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class ViewingManagement {
    private Queue<Model.Request.ViewingRequest> viewingRequests;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;

    public ViewingManagement() {
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        loadViewingRequests();
    }

    // Load viewing requests from JSON file
    private void loadViewingRequests() {
        viewingRequests = DatabaseManagement.getViewingRequests();

        if (viewingRequests == null) {
            viewingRequests = new LinkedList<>();
        }
    }

    // Save viewing requests to JSON file
    private void saveViewingRequests() {
        DatabaseManagement.saveViewingRequests(viewingRequests);
    }

    // Load rooms from JSON file
    private LinkedList<Room> loadRooms() {
        return DatabaseManagement.getRooms();
    }

    // Load applicants from JSON file
    private LinkedList<Applicant> loadApplicants() {
        LinkedList<Applicant> applicants = new LinkedList<>();
        for (Model.User.User user : Model.User.User.getUsers()) {
            if (user instanceof Applicant) {
                applicants.add((Applicant) user);
            }
        }
        return applicants;
    }

    // Generate a unique request ID
    private String generateRequestID() {
        return "VR" + (viewingRequests.size() + 1);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=== VIEWING REQUEST MANAGEMENT ===");
            System.out.println("1. View & Manage All Viewing Requests");
            System.out.println("2. View Viewing History (Sorted by Status)");
            System.out.println("3. Schedule New Room Viewing (Applicant)");
            System.out.println("4. Cancel/Reschedule Viewing (Applicant)");
            System.out.println("5. Back to Previous Menu");
            System.out.print("Choose an option: ");

            int choice = InputValidator.getMenuChoice(5);

            switch (choice) {
                case 1:
                    viewAndManageRequestsAdmin();
                    break;
                case 2:
                    viewViewingHistory();
                    break;
                case 3:
                    scheduleRoomViewing();
                    break;
                case 4:
                    cancelRescheduleViewing();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewAndManageRequestsAdmin() {
        System.out.println("\n=== ALL VIEWING REQUESTS ===");

        // Filter for pending and scheduled requests
        Queue<Model.Request.ViewingRequest> activeRequests = new LinkedList<>();
        for (Model.Request.ViewingRequest request : viewingRequests) {
            RequestStatus status = request.getRequestStatus();
            if (status == RequestStatus.PENDING || status == RequestStatus.RESCHEDULED) {
                activeRequests.add(request);
            }
        }

        if (activeRequests.isEmpty()) {
            System.out.println("No active viewing requests found.");
            return;
        }

        displayViewingRequests(activeRequests, false);
        manageSelectedRequest(activeRequests);
    }

    private void viewViewingHistory() {
        System.out.println("\n=== VIEWING HISTORY ===");
        System.out.println("1. View All History");
        System.out.println("2. View Approved");
        System.out.println("3. View Declined");
        System.out.println("4. View Rescheduled");
        System.out.println("5. Back");
        System.out.print("Choose an option: ");

        int choice = InputValidator.getMenuChoice(5);

        Queue<Model.Request.ViewingRequest> historyRequests = new LinkedList<>();

        switch (choice) {
            case 1:
                historyRequests = viewingRequests;
                break;
            case 2:
                for (Model.Request.ViewingRequest request : viewingRequests) {
                    if (request.getRequestStatus() == RequestStatus.APPROVED) {
                        historyRequests.add(request);
                    }
                }
                break;
            case 3:
                for (Model.Request.ViewingRequest request : viewingRequests) {
                    if (request.getRequestStatus() == RequestStatus.DECLINED) {
                        historyRequests.add(request);
                    }
                }
                break;
            case 4:
                for (Model.Request.ViewingRequest request : viewingRequests) {
                    if (request.getRequestStatus() == RequestStatus.RESCHEDULED) {
                        historyRequests.add(request);
                    }
                }
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (historyRequests.isEmpty()) {
            System.out.println("No viewing requests found for the selected filter.");
            return;
        }

        displayViewingRequests(historyRequests, true);

        if (choice >= 2 && choice <= 4) {
            System.out.println("\nNote: Rescheduled requests are waiting for applicant availability confirmation.");
        }
    }

    private void displayViewingRequests(Iterable<Model.Request.ViewingRequest> requests, boolean showHistory) {
        System.out.println("\n" + "=".repeat(120));
        System.out.printf("%-10s %-20s %-15s %-20s %-15s %-10s\n",
                "Request ID", "Applicant", "Room Number", "Scheduled Date", "Status", "Admin Action");
        System.out.println("=".repeat(120));

        for (Model.Request.ViewingRequest request : requests) {
            String adminAction = "N/A";
            RequestStatus status = request.getRequestStatus();

            if (!showHistory && status == RequestStatus.PENDING) {
                adminAction = "ACTION REQUIRED";
            } else if (status == RequestStatus.RESCHEDULED) {
                adminAction = "WAITING CONFIRMATION";
            }

            System.out.printf("%-10s %-20s %-15s %-20s %-15s %-10s\n",
                    request.getRequestID(),
                    request.getApplicantID().getFullName(),
                    request.getRoomID().getRoomNumber(),
                    dateFormat.format(request.getScheduledDate()),
                    status,
                    adminAction);
        }
        System.out.println("=".repeat(120));
    }

    private void manageSelectedRequest(Iterable<Model.Request.ViewingRequest> requests) {
        System.out.print("\nEnter Request ID to manage (or 0 to go back): ");
        String requestId = scanner.next();

        if (requestId.equals("0")) {
            return;
        }

        Model.Request.ViewingRequest selectedRequest = null;
        for (Model.Request.ViewingRequest request : requests) {
            if (request.getRequestID().equals(requestId)) {
                selectedRequest = request;
                break;
            }
        }

        if (selectedRequest == null) {
            System.out.println("Invalid Request ID.");
            return;
        }

        RequestStatus status = selectedRequest.getRequestStatus();
        if (status != RequestStatus.PENDING && status != RequestStatus.RESCHEDULED) {
            System.out.println("This request is no longer pending management.");
            return;
        }

        showManagementOptions(selectedRequest);
    }

    private void showManagementOptions(Model.Request.ViewingRequest request) {
        System.out.println("\n=== MANAGING REQUEST #" + request.getRequestID() + " ===");
        request.displayRequest();

        System.out.println("\nManagement Options:");
        System.out.println("1. Approve Request");
        System.out.println("2. Decline Request");
        System.out.println("3. Reschedule Request");
        System.out.println("4. Back to List");
        System.out.print("Choose an option: ");

        int choice = InputValidator.getMenuChoice(4);

        switch (choice) {
            case 1:
                approveRequest(request);
                break;
            case 2:
                declineRequest(request);
                break;
            case 3:
                rescheduleRequest(request);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void approveRequest(Model.Request.ViewingRequest request) {
        System.out.print("Enter any notes for the applicant (or press enter to skip): ");
        scanner.nextLine(); // Clear buffer
        scanner.nextLine();

        request.setRequestStatus(RequestStatus.APPROVED);

        // You might want to add notes to your ViewingRequest model
        // request.setAdminNotes(notes);

        saveViewingRequests();
        System.out.println("Request approved successfully!");

        // Send notification to applicant
        sendNotification(request.getApplicantID(),
                "Your viewing request for room " + request.getRoomID().getRoomNumber() +
                        " has been approved for " + dateFormat.format(request.getScheduledDate()));
    }

    private void declineRequest(Model.Request.ViewingRequest request) {
        System.out.print("Enter reason for declining: ");
        scanner.nextLine(); // Clear buffer
        String reason = scanner.nextLine();

        if (reason.trim().isEmpty()) {
            System.out.println("Reason is required for declining a request.");
            return;
        }

        request.setRequestStatus(RequestStatus.DECLINED);
        // request.setAdminNotes("DECLINED: " + reason);

        saveViewingRequests();
        System.out.println("Request declined successfully!");

        // Send notification to applicant
        sendNotification(request.getApplicantID(),
                "Your viewing request for room " + request.getRoomID().getRoomNumber() +
                        " has been declined. Reason: " + reason);
    }

    private void rescheduleRequest(Model.Request.ViewingRequest request) {
        System.out.println("\n=== RESCHEDULE REQUEST ===");

        System.out.print("Enter proposed new date (YYYY-MM-DD): ");
        String dateStr = scanner.next();

        System.out.print("Enter proposed new time (HH:mm): ");
        String timeStr = scanner.next();

        String newDateTimeStr = dateStr + " " + timeStr;

        try {
            Date newDate = dateFormat.parse(newDateTimeStr);

            // Check if the new date is in the future
            if (newDate.before(new Date())) {
                System.out.println("Cannot schedule viewing in the past.");
                return;
            }

            System.out.print("Enter reason for rescheduling: ");
            scanner.nextLine(); // Clear buffer
            String reason = scanner.nextLine();

            request.setScheduledDate(newDate);
            request.setRequestStatus(RequestStatus.RESCHEDULED);
            // request.setAdminNotes("RESCHEDULED from " + dateFormat.format(originalDate) +
            // " to " +
            // dateFormat.format(newDate) + ". Reason: " + reason);

            saveViewingRequests();
            System.out.println("Request marked as rescheduled! Waiting for applicant confirmation.");
            System.out.println("Reason: " + reason);

            // Send notification to applicant
            sendNotification(request.getApplicantID(),
                    "Your viewing request for room " + request.getRoomID().getRoomNumber() +
                            " has been rescheduled to " + dateFormat.format(newDate) +
                            ". Please confirm your availability. Reason: " + reason);

        } catch (Exception e) {
            System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:mm format.");
        }
    }

    private void scheduleRoomViewing() {
        System.out.println("\n=== SCHEDULE NEW ROOM VIEWING ===");

        // Display available rooms (vacant only)
        LinkedList<Room> rooms = loadRooms();
        LinkedList<Room> availableRooms = new LinkedList<>();

        for (Room room : rooms) {
            if (room.getStatus() == RoomStatus.Vacant) {
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for viewing at the moment.");
            return;
        }

        System.out.println("\nAvailable Rooms:");
        System.out.println("=".repeat(80));
        System.out.printf("%-5s %-15s %-10s %-10s %-10s\n",
                "No.", "Room Number", "Type", "Price", "Capacity");
        System.out.println("=".repeat(80));

        int index = 1;
        for (Room room : availableRooms) {
            System.out.printf("%-5d %-15s %-10s %-10.2f %-10d\n",
                    index++,
                    room.getRoomNumber(),
                    room.getType(),
                    room.getPrice(),
                    room.getCapacity());
        }
        System.out.println("=".repeat(80));

        int roomChoice = InputValidator.getValidInt(1, availableRooms.size(), "Select room number");

        if (roomChoice == -1) {
            System.out.println("Room selection cancelled.");
            return;
        }

        Room selectedRoom = availableRooms.get(roomChoice - 1);

        // Select applicant
        LinkedList<Applicant> applicants = loadApplicants();
        if (applicants.isEmpty()) {
            System.out.println("No applicants found. Please register as an applicant first.");
            return;
        }

        System.out.println("\nSelect Applicant:");
        for (int i = 0; i < applicants.size(); i++) {
            System.out.println((i + 1) + ". " + applicants.get(i).getFullName());
        }

        int applicantChoice = InputValidator.getValidInt(1, applicants.size(), "Select applicant");

        if (applicantChoice == -1) {
            System.out.println("Applicant selection cancelled.");
            return;
        }

        Applicant selectedApplicant = applicants.get(applicantChoice - 1);

        // Schedule date and time
        System.out.print("Enter preferred date (YYYY-MM-DD): ");
        String dateStr = scanner.next();

        System.out.print("Enter preferred time (HH:mm): ");
        String timeStr = scanner.next();

        String dateTimeStr = dateStr + " " + timeStr;

        try {
            Date scheduledDate = dateFormat.parse(dateTimeStr);

            // Check if date is in the future
            if (scheduledDate.before(new Date())) {
                System.out.println("Cannot schedule viewing in the past.");
                return;
            }

            // Create new viewing request
            String requestID = generateRequestID();
            Model.Request.ViewingRequest newRequest = new Model.Request.ViewingRequest(
                    requestID,
                    selectedRoom,
                    RequestStatus.PENDING,
                    selectedApplicant,
                    scheduledDate);

            viewingRequests.add(newRequest);
            saveViewingRequests();

            System.out.println("\nViewing request created successfully!");
            System.out.println("Request ID: " + requestID);
            System.out.println("Room: " + selectedRoom.getRoomNumber());
            System.out.println("Scheduled for: " + dateFormat.format(scheduledDate));
            System.out.println("Status: PENDING (Waiting for admin approval)");

        } catch (Exception e) {
            System.out.println("Invalid date/time format. Please use YYYY-MM-DD HH:mm format.");
        }
    }

    private void cancelRescheduleViewing() {
        System.out.println("\n=== CANCEL/RESCHEDULE VIEWING ===");

        // Get current user (applicant) - in a real system, you'd get from session
        System.out.print("Enter your Applicant ID: ");
        String applicantId = scanner.next();

        // Find requests for this applicant
        Queue<Model.Request.ViewingRequest> applicantRequests = new LinkedList<>();
        for (Model.Request.ViewingRequest request : viewingRequests) {
            if (request.getApplicantID().getUserID().equals(applicantId)) {
                applicantRequests.add(request);
            }
        }

        if (applicantRequests.isEmpty()) {
            System.out.println("No viewing requests found for this applicant.");
            return;
        }

        System.out.println("\nYour Viewing Requests:");
        displayViewingRequests(applicantRequests, true);

        System.out.print("Enter Request ID to manage (or 0 to go back): ");
        String requestId = scanner.next();

        if (requestId.equals("0")) {
            return;
        }

        Model.Request.ViewingRequest selectedRequest = null;
        for (Model.Request.ViewingRequest request : applicantRequests) {
            if (request.getRequestID().equals(requestId)) {
                selectedRequest = request;
                break;
            }
        }

        if (selectedRequest == null) {
            System.out.println("Invalid Request ID.");
            return;
        }

        System.out.println("\nOptions for Request #" + requestId + ":");
        System.out.println("1. Cancel Viewing Request");
        System.out.println("2. Request Reschedule");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");

        int choice = InputValidator.getMenuChoice(3);

        switch (choice) {
            case 1:
                cancelRequest(selectedRequest);
                break;
            case 2:
                requestReschedule(selectedRequest);
                break;
            case 3:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void cancelRequest(Model.Request.ViewingRequest request) {
        System.out.print("Enter reason for cancellation: ");
        scanner.nextLine(); // Clear buffer
        String reason = scanner.nextLine();

        request.setRequestStatus(RequestStatus.DECLINED);
        // request.setApplicantNotes("CANCELLED by applicant: " + reason);

        saveViewingRequests();
        System.out.println("Viewing request cancelled successfully.");
        System.out.println("Reason: " + reason);
    }

    private void requestReschedule(Model.Request.ViewingRequest request) {
        System.out.println("\n=== REQUEST RESCHEDULE ===");

        System.out.print("Enter preferred new date (YYYY-MM-DD): ");
        String dateStr = scanner.next();

        System.out.print("Enter preferred new time (HH:mm): ");
        String timeStr = scanner.next();

        String newDateTimeStr = dateStr + " " + timeStr;

        try {
            Date newDate = dateFormat.parse(newDateTimeStr);

            System.out.print("Enter reason for rescheduling: ");
            scanner.nextLine(); // Clear buffer
            String reason = scanner.nextLine();

            // Create a new reschedule request
            request.setRequestStatus(RequestStatus.RESCHEDULED);
            request.setScheduledDate(newDate);
            // request.setApplicantNotes("RESCHEDULE REQUESTED: " + reason);

            saveViewingRequests();
            System.out.println("Reschedule request submitted. Waiting for admin approval.");
            System.out.println("Reason: " + reason);

        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
        }
    }

    private void sendNotification(Applicant applicant, String message) {
        // In a real system, you would send email or SMS notification
        System.out.println("\n=== NOTIFICATION SENT TO " + applicant.getFullName() + " ===");
        System.out.println("Message: " + message);
        System.out.println("Contact: " + applicant.getContactNumber());
    }
}

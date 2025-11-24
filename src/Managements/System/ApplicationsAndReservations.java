package Managements.System;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import src.Model.Contract.Application;

public class ApplicationsAndReservations {

    Scanner input = new Scanner(System.in);
    LinkedList<Application> applicationList;
    int choice;

    public ApplicationsAndReservations(LinkedList<Application> applicationList) {
        this.applicationList = (applicationList != null) ? applicationList : new LinkedList<>();
    }

    public void displayMenu() {
        do {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("      APPLICATIONS AND RESERVATIONS MENU       ");
            System.out.println("───────────────────────────────────────────────");
            System.out.println("[1] View Pending Applications");
            System.out.println("[2] Update Applications");
            System.out.println("[3] Confirm Reservation Payment (Convert to Tenant)");
            System.out.println("[4] Exit");
            System.out.println("───────────────────────────────────────────────");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();
                input.nextLine();

                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid input. Please enter 1-4 only.");
                    continue;
                }

                handleLandlordChoice(choice);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers only.");
                input.nextLine();
            }

        } while (choice != 4);

        System.out.println("Exiting Applications and Reservations...");
    }

    public void handleLandlordChoice(int choice) {
        switch (choice) {
            case 1:
                viewPendingApplications();
                break;
            case 2:
                updateApplications();
                break;
            case 3:
                confirmReservationPayment();
                break;
        }
    }

    public void viewPendingApplications() {
        System.out.println("────────── PENDING APPLICATIONS ──────────");

        boolean found = false;

        for (int i = 0; i < applicationList.size(); i++) {
            Application app = applicationList.get(i); // LinkedList action

            if (app.getApplicationStatus().equalsIgnoreCase("pending")) {
                found = true;
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Applicant ID: " + app.getApplicantID());
                System.out.println("Preferred Room: " + app.getPreferredRoomID());
                System.out.println("Date Submitted: " + app.getDateSubmitted());
                System.out.println("Initial Payment: " + (app.isInitialPaymentMade() ? "Yes" : "No"));
                System.out.println("Status: " + app.checkStatus());
                System.out.println("--------------------------------------");
            }
        }

        if (!found) {
            System.out.println("No pending applications at the moment.");
        }

        System.out.println("────────────────────────────────────────\n");
    }

    public void updateApplications() {
        System.out.println("\n────────── UPDATE APPLICATIONS ──────────");

        System.out.print("Enter Application ID to update: ");
        String appID = input.nextLine();

        Application app = findApplicationById(appID);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }

        System.out.println("\nSelected Application:");
        System.out.println("Applicant ID: " + app.getApplicantID());
        System.out.println("Preferred Room: " + app.getPreferredRoomID());
        System.out.println("Status: " + app.getApplicationStatus());
        System.out.println("--------------------------------------");

        System.out.println("[1] Approve Application");
        System.out.println("[2] Reject Application");
        System.out.println("[3] Change Preferred Room");
        System.out.println("[4] Cancel");
        System.out.print("Enter choice: ");

        int action = input.nextInt();
        input.nextLine();

        switch (action) {
            case 1:
                app.approve();
                break;

            case 2:
                if (app.reject()) {
                    System.out.println("Application rejected successfully.");
                } else {
                    System.out.println("Cannot reject. Application is already approved or rejected.");
                }
                break;

            case 3:
                System.out.print("Enter new preferred room ID: ");
                String newRoom = input.nextLine();
                app.setPreferredRoomID(newRoom);
                System.out.println("Preferred room updated successfully.");
                break;

            case 4:
                System.out.println("Cancelled.");
                break;

            default:
                System.out.println("Invalid choice.");
        }

        System.out.println("────────────────────────────────────────\n");
    }

    public void confirmReservationPayment() {
        System.out.println("\n──────── CONFIRM RESERVATION PAYMENT ────────");

        System.out.print("Enter Application ID: ");
        String appID = input.nextLine();

        Application app = findApplicationById(appID);

        if (app == null) {
            System.out.println("Application not found.");
            return;
        }

        if (!app.getApplicationStatus().equalsIgnoreCase("approved")) {
            System.out.println("You can only confirm payments for APPROVED applications.");
            return;
        }

        if (app.isInitialPaymentMade()) {
            System.out.println("Initial payment already recorded.");
            return;
        }

        System.out.println("Applicant: " + app.getApplicantID());
        System.out.print("Confirm payment? (yes/no): ");
        String confirm = input.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            app.setInitialPaymentMade(true);
            System.out.println("Payment confirmed.");
            System.out.println("Application is now marked as: Ready for move-in.");
        } else {
            System.out.println("Payment not confirmed.");
        }

        System.out.println("────────────────────────────────────────\n");
    }

    private Application findApplicationById(String id) {
        for (Application app : applicationList) {
            if (app.getApplicationID().equalsIgnoreCase(id)) {
                return app;
            }
        }
        return null;
    }
}

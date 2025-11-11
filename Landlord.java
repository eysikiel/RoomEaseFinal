
import java.util.InputMismatchException;
import java.util.Scanner;

public class Landlord extends User {

    int choice;

    private Scanner input = new Scanner(System.in);

    private String landlordID;
    private RoomManagement roomManagement;
    private TenantManagement tenantManagement;
    private BillingAndPayments billingAndPayments;
    private ViewingRequests viewingRequests;
    private ApplicationsAndReservations applicationsAndReservations;
    private MaintenanceRequests maintenanceRequests;
    private ReportsAndAnalytics reportsAndAnalytics;
    private SystemSettings systemSettings;

    public Landlord(ApplicationsAndReservations applicationsAndReservations, BillingAndPayments billingAndPayments, String landlordID, MaintenanceRequests maintenanceRequests, ReportsAndAnalytics reportsAndAnalytics, RoomManagement roomManagement, SystemSettings systemSettings, TenantManagement tenantManagement, ViewingRequests viewingRequests, String contactNumber, String firstName, String lastName, String password, String userID, String username, Role role) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.applicationsAndReservations = applicationsAndReservations;
        this.billingAndPayments = billingAndPayments;
        this.landlordID = landlordID;
        this.maintenanceRequests = maintenanceRequests;
        this.reportsAndAnalytics = reportsAndAnalytics;
        this.roomManagement = roomManagement;
        this.systemSettings = systemSettings;
        this.tenantManagement = tenantManagement;
        this.viewingRequests = viewingRequests;
    }

    public int getChoice() {
        return choice;
    }

    public Scanner getInput() {
        return input;
    }

    public String getLandlordID() {
        return landlordID;
    }

    public RoomManagement getRoomManagement() {
        return roomManagement;
    }

    public TenantManagement getTenantManagement() {
        return tenantManagement;
    }

    public BillingAndPayments getBillingAndPayments() {
        return billingAndPayments;
    }

    public ViewingRequests getViewingRequests() {
        return viewingRequests;
    }

    public ApplicationsAndReservations getApplicationsAndReservations() {
        return applicationsAndReservations;
    }

    public MaintenanceRequests getMaintenanceRequests() {
        return maintenanceRequests;
    }

    public ReportsAndAnalytics getReportsAndAnalytics() {
        return reportsAndAnalytics;
    }

    public SystemSettings getSystemSettings() {
        return systemSettings;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public void setLandlordID(String landlordID) {
        this.landlordID = landlordID;
    }

    public void setRoomManagement(RoomManagement roomManagement) {
        this.roomManagement = roomManagement;
    }

    public void setTenantManagement(TenantManagement tenantManagement) {
        this.tenantManagement = tenantManagement;
    }

    public void setBillingAndPayments(BillingAndPayments billingAndPayments) {
        this.billingAndPayments = billingAndPayments;
    }

    public void setViewingRequests(ViewingRequests viewingRequests) {
        this.viewingRequests = viewingRequests;
    }

    public void setApplicationsAndReservations(ApplicationsAndReservations applicationsAndReservations) {
        this.applicationsAndReservations = applicationsAndReservations;
    }

    public void setMaintenanceRequests(MaintenanceRequests maintenanceRequests) {
        this.maintenanceRequests = maintenanceRequests;
    }

    public void setReportsAndAnalytics(ReportsAndAnalytics reportsAndAnalytics) {
        this.reportsAndAnalytics = reportsAndAnalytics;
    }

    public void setSystemSettings(SystemSettings systemSettings) {
        this.systemSettings = systemSettings;
    }

    public void logout() {
    }

    public void accessRoomManagement() {

    }

    public void accessTenantManagement() {
    }

    public void accessBilling() {
    }

    public void accessViewingRequests() {
    }

    public void accessApplications() {
    }

    public void accessMaintenance() {
    }

    public void accessReports() {
    }

    public void accessSystemSettings() {
    }

    /*@Override
    public boolean verifyLogin(String username, String password) {
        return false;
    }

    @Override
    public void displayProfile() {
    } */

    public void displayLandlordMenu() {

        do {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("               LANDLORD MAIN MENU              ");
            System.out.println("───────────────────────────────────────────────");
            System.out.println("[1] Room Management");
            System.out.println("[2] Tenant Management");
            System.out.println("[3] Applications and Reservations");
            System.out.println("[4] Viewing Requests");
            System.out.println("[5] Maintenance Requests");
            System.out.println("[6] Contract Management");
            System.out.println("[7] Billing and Payments");
            System.out.println("[8] Reports and Analytics");
            System.out.println("[9] System Settings");
            System.out.println("[10] Log out");
            System.out.println("───────────────────────────────────────────────");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();

                if (choice < 1 || choice > 10) {
                    System.out.println("Invalid input. Please enter a number between 1 and 10.\n");
                    continue;
                }
                handleLandlordChoice(choice);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number only.");
                input.nextLine();
            }

        } while (choice != 10);
    }

    public void handleLandlordChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.println("Opening Room Management...");
            // roomManagement.displaySubmenu();
            case 2:
                System.out.println("Opening Tenant Management...");
            case 3:
                System.out.println("Opening Applications and Reservations...");
            case 4:
                System.out.println("Opening Viewing Requests...");
            case 5:
                System.out.println("Opening Maintenance Requests...");
            case 6:
                System.out.println("Opening Contract Management...");
            case 7:
                System.out.println("Opening Billing and Payments...");
            case 8:
                System.out.println("Opening Reports and Analytics...");
            case 9:
                System.out.println("Opening System Settings...");
            case 10:
                System.out.println("Logging out... Goodbye!");
            default:
                System.out.println("Invalid choice. Please try again.");

        }

    }

}

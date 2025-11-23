
import java.util.InputMismatchException;
import java.util.Scanner;

public class Landlord extends User {

    int choice;

    private Scanner input = new Scanner(System.in);

    private String landlordID;
    private RoomManagement roomManagement;
    private TenantManagement tenantManagement;
    private BillingAndPayments billingAndPayments;
    private ViewingManagement viewingManagement;
    private ApplicationsAndReservations applicationsAndReservations;
    private MaintenanceManagement maintenanceManagement;
    private ReportsAndAnalytics reportsAndAnalytics;
    private SystemSettings systemSettings;

    public Landlord(ApplicationsAndReservations applicationsAndReservations, BillingAndPayments billingAndPayments, String landlordID, MaintenanceManagement maintenanceManagement, ReportsAndAnalytics reportsAndAnalytics, RoomManagement roomManagement, SystemSettings systemSettings, TenantManagement tenantManagement, ViewingManagement viewingManagement, String contactNumber, String firstName, String lastName, String password, String userID, String username, Role role) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.applicationsAndReservations = applicationsAndReservations;
        this.billingAndPayments = billingAndPayments;
        this.landlordID = landlordID;
        this.maintenanceManagement = maintenanceManagement;
        this.reportsAndAnalytics = reportsAndAnalytics;
        this.systemSettings = systemSettings;
        this.tenantManagement = tenantManagement;
        this.viewingManagement = viewingManagement;
    }

    public Landlord(String contactNumber, String firstName, String lastName, String password, String userID, String username, User.Role role) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.landlordID = userID;
        this.roomManagement = new RoomManagement();
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
        if (this.roomManagement == null) {
            this.roomManagement = new RoomManagement();
        }
        return this.roomManagement;
    }

    public TenantManagement getTenantManagement() {
        return tenantManagement;
    }

    public BillingAndPayments getBillingAndPayments() {
        return billingAndPayments;
    }

    public ViewingManagement getViewingRequests() {
        return viewingManagement;
    }

    public ApplicationsAndReservations getApplicationsAndReservations() {
        return applicationsAndReservations;
    }

    public MaintenanceManagement getMaintenanceRequests() {
        return maintenanceManagement;
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

    public void setViewingRequests(ViewingManagement viewingManagement) {
        this.viewingManagement = viewingManagement;
    }

    public void setApplicationsAndReservations(ApplicationsAndReservations applicationsAndReservations) {
        this.applicationsAndReservations = applicationsAndReservations;
    }

    public void setMaintenanceRequests(MaintenanceManagement maintenanceManagement) {
        this.maintenanceManagement = maintenanceManagement;
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

    @Override
    public void displayRoleMenu() {
        displayLandlordMenu();
    }

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
                    System.out.println("Invalid input. Please enter a number between 1 and 10.");
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
                getRoomManagement().displayMenu();
                break;
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

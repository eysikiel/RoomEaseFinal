package Model.User;

import Utilities.InputValidator;
import Managements.RoomManagement;
import Managements.TenantManagement;
import Database.DatabaseManagement;
import Managements.ContractManagement;

public class Landlord extends User {

    private RoomManagement roomManagement;
    private TenantManagement tenantManagement; // Add this
    private DatabaseManagement databaseManager;
    private ContractManagement contractManager;

    // 7-parameter constructor
    public Landlord(String contactNumber, String firstName, String lastName, String password, String userID,
            String username, User.Role role) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.databaseManager = new DatabaseManagement();
        this.roomManagement = new RoomManagement();
        this.tenantManagement = new TenantManagement(); // Initialize tenant management
        this.contractManager = new ContractManagement();
    }

    // Getters and Setters
    public RoomManagement getRoomManagement() {
        return this.roomManagement;
    }

    public DatabaseManagement getDatabaseManager() {
        return databaseManager;
    }

    public TenantManagement getTenantManagement() {
        return this.tenantManagement;
    }

    public ContractManagement getContractManagement() {
        return this.contractManager;
    }

    public void setRoomManagement(RoomManagement roomManagement) {
        this.roomManagement = roomManagement;
    }

    public void setDatabaseManager(DatabaseManagement databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Implemented methods
    public void logout() {
        System.out.println("Landlord " + getUsername() + " logged out successfully.");
    }

    @Override
    public void displayProfile() {
        System.out.println("-------------------------------------------------");
        System.out.println("                  LANDLORD PROFILE               ");
        System.out.println("-------------------------------------------------");
        System.out.println("Name:           " + getFirstName() + " " + getLastName());
        System.out.println("Username:       " + getUsername());
        System.out.println("Contact:        " + getContactNumber());
        System.out.println("Landlord ID:    " + getUserID()); // Now shows LND001
        System.out.println("Role:           " + getRole());
        System.out.println("-------------------------------------------------");
    }

    public void accessRoomManagement() {
        getRoomManagement().displayMenu();
    }

    public void accessTenantManagement() {
        System.out.println("Opening Tenant Management...");
        getTenantManagement().displayMenu(); // Call instance method
    }

    public void accessBilling() {
        System.out.println("Opening Billing and Payments...");
        System.out.println("Billing and Payments feature coming soon!");
    }

    public void accessViewingRequests() {
        System.out.println("Opening Viewing Requests...");
        System.out.println("Viewing Requests feature coming soon!");
    }

    public void accessApplications() {
        System.out.println("Opening Applications and Reservations...");
        System.out.println("Applications and Reservations feature coming soon!");
    }

    public void accessMaintenance() {
        System.out.println("Opening Maintenance Requests...");
        System.out.println("Maintenance Requests feature coming soon!");
    }

    public void accessReports() {
        System.out.println("Opening Reports and Analytics...");
        System.out.println("Reports and Analytics feature coming soon!");
    }

    public void accessSystemSettings() {
        System.out.println("Opening System Settings...");
        System.out.println("System Settings feature coming soon!");
    }

    public void accessContractManagement() {
        System.out.println("Opening Contract Management...");
        getContractManagement().displayMenu();
    }

    @Override
    public void displayRoleMenu() {
        displayLandlordMenu();
    }

    public void displayLandlordMenu() {
        int choice;
        do {
            // DISPLAY THE MENU FIRST
            System.out.println("-------------------------------------------------");
            System.out.println("               LANDLORD MAIN MENU                ");
            System.out.println("-------------------------------------------------");
            System.out.println("[1] Room Management");
            System.out.println("[2] Tenant Management");
            System.out.println("[3] Applications and Reservations");
            System.out.println("[4] Viewing Requests");
            System.out.println("[5] Maintenance Requests");
            System.out.println("[6] Contract Management");
            System.out.println("[7] Billing and Payments");
            System.out.println("[8] Reports and Analytics");
            System.out.println("[9] System Settings");
            System.out.println("[10] View My Profile");
            System.out.println("[11] Edit My Profile");
            System.out.println("[12] Log out");
            System.out.println("-------------------------------------------------");

            // THEN GET THE CHOICE (NO MENU TITLE PARAMETER)
            choice = InputValidator.getMenuChoice(12);
            if (choice == -1) {
                continue; // User cancelled, show menu again
            }
            handleLandlordChoice(choice);
        } while (choice != 12);
    }

    public void handleLandlordChoice(int choice) {
        switch (choice) {
            case 1:
                accessRoomManagement();
                break;
            case 2:
                accessTenantManagement();
                break;
            case 3:
                accessApplications();
                break;
            case 4:
                accessViewingRequests();
                break;
            case 5:
                accessMaintenance();
                break;
            case 6:
                accessContractManagement();
                break;
            case 7:
                accessBilling();
                break;
            case 8:
                accessReports();
                break;
            case 9:
                accessSystemSettings();
                break;
            case 10:
                System.out.println("Displaying Profile...");
                displayProfile();
                break;
            case 11:
                System.out.println("Editing Profile...");
                editProfile();
                break;
            case 12:
                System.out.println("Logging out... Goodbye!");
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void editProfile() {
        System.out.println("-------------------------------------------------");
        System.out.println("               EDIT LANDLORD PROFILE            ");
        System.out.println("-------------------------------------------------");

        // Edit First Name
        String firstName = InputValidator.getValidName("Enter First Name (current: " + getFirstName() + ")");
        if (firstName != null) {
            setFirstName(firstName);
        }

        // Edit Last Name
        String lastName = InputValidator.getValidName("Enter Last Name (current: " + getLastName() + ")");
        if (lastName != null) {
            setLastName(lastName);
        }

        // Edit Contact Number
        String contactNumber = InputValidator
                .getValidPHContactNumber("Enter Contact Number (current: " + getContactNumber() + ")");
        if (contactNumber != null) {
            setContactNumber(contactNumber);
        }

        // Edit Password with confirmation
        Boolean changePassword = InputValidator.getConfirmation("Do you want to change your password?");
        if (changePassword != null && changePassword) {
            String newPassword = InputValidator.getStringWithMinLength("Enter New Password", 6);
            if (newPassword != null) {
                Boolean confirmPassword = InputValidator
                        .getConfirmation("Are you sure you want to change your password?");
                if (confirmPassword != null && confirmPassword) {
                    setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                }
            }
        }

        System.out.println("Profile updated successfully!");
    }

    @Override
    public boolean verifyLogin(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }
}
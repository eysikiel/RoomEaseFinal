package Managements;

import Utilities.InputValidator;
import Model.User.User;
import Enums.RoomStatus;
import Model.User.Tenant;
import Model.Property.Room;
import Database.DatabaseManagement;
import java.util.LinkedList;

public class TenantManagement {

    public TenantManagement() {
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("              TENANT MANAGEMENT                  ");
            System.out.println("-------------------------------------------------");
            System.out.println("[1] View Tenants");
            System.out.println("[2] Sort Tenants by Balance");
            System.out.println("[3] Register New Tenant");
            System.out.println("[4] Assign Room to Tenant");
            System.out.println("[5] Remove Tenant");
            System.out.println("[6] View Tenant Records");
            System.out.println("[7] Back to Main Menu");
            System.out.println("-------------------------------------------------");

            choice = InputValidator.getMenuChoice(7);
            if (choice == -1) {
                continue;
            }

            switch (choice) {
                case 1:
                    viewTenants();
                    break;
                case 2:
                    sortTenantsByBalance();
                    break;
                case 3:
                    registerNewTenant();
                    break;
                case 4:
                    assignRoom();
                    break;
                case 5:
                    removeTenant();
                    break;
                case 6:
                    viewTenantRecords();
                    break;
                case 7:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }

    public void viewTenants() {
        System.out.println("-------------------------------------------------");
        System.out.println("                  VIEW TENANTS                   ");
        System.out.println("-------------------------------------------------");

        LinkedList<Tenant> tenants = getTenants();
        if (tenants.isEmpty()) {
            System.out.println("No tenants registered yet.");
            return;
        }

        for (Tenant tenant : tenants) {
            System.out.println("Tenant ID:    " + tenant.getTenantID());
            System.out.println("Name:         " + tenant.getFullName());
            System.out.println("Room ID:      " + (tenant.getRoomID() != null ? tenant.getRoomID() : "Not assigned"));
            System.out.println("Balance:      ₱" + String.format("%.2f", tenant.getBalance()));
            System.out.println("Contact:      " + tenant.getContactNumber());
            System.out.println("-------------------------------------------------");
        }
    }

    public void sortTenantsByBalance() {
        System.out.println("-------------------------------------------------");
        System.out.println("            SORT TENANTS BY BALANCE             ");
        System.out.println("-------------------------------------------------");

        System.out.println("Sort order:");
        System.out.println("[1] Ascending (Lowest to Highest)");
        System.out.println("[2] Descending (Highest to Lowest)");

        int sortChoice = InputValidator.getMenuChoice(2);
        if (sortChoice == -1) {
            return;
        }

        LinkedList<Tenant> tenants = getTenants();
        if (tenants.isEmpty()) {
            System.out.println("No tenants to sort.");
            return;
        }

        // Simple bubble sort based on choice
        for (int i = 0; i < tenants.size() - 1; i++) {
            for (int j = 0; j < tenants.size() - i - 1; j++) {
                boolean shouldSwap = (sortChoice == 1)
                        ? tenants.get(j).getBalance() > tenants.get(j + 1).getBalance()
                        : tenants.get(j).getBalance() < tenants.get(j + 1).getBalance();

                if (shouldSwap) {
                    Tenant temp = tenants.get(j);
                    tenants.set(j, tenants.get(j + 1));
                    tenants.set(j + 1, temp);
                }
            }
        }

        System.out.println("Tenants sorted " + (sortChoice == 1 ? "ascending" : "descending") + " by balance:");
        for (Tenant tenant : tenants) {
            System.out.println(tenant.getTenantID() + " - " + tenant.getFullName()
                    + " - Balance: ₱" + String.format("%.2f", tenant.getBalance()));
        }
    }

    public void registerNewTenant() {
        System.out.println("-------------------------------------------------");
        System.out.println("             REGISTER NEW TENANT                 ");
        System.out.println("-------------------------------------------------");

        try {
            // Generate IDs
            String tenantID = Tenant.generateNextTenantID();
            String userID = User.generateNextUserID(User.Role.TENANT);

            // Get tenant information using InputValidator
            String firstName = InputValidator.getValidName("Enter First Name");
            if (firstName == null) {
                return;
            }

            String lastName = InputValidator.getValidName("Enter Last Name");
            if (lastName == null) {
                return;
            }

            String username = InputValidator.getNonEmptyString("Enter Username");
            if (username == null) {
                return;
            }

            String password = InputValidator.getStringWithMinLength("Enter Password", 6);
            if (password == null) {
                return;
            }

            String contactNumber = InputValidator.getValidPHContactNumber("Enter Contact Number (+63 9XX-XXX-XXXX)  ");
            if (contactNumber == null) {
                return;
            }

            String idNumber = InputValidator.getNonEmptyString("Enter ID Number");
            if (idNumber == null) {
                return;
            }

            String emergencyContact = InputValidator.getValidPHContactNumber("Enter Emergency Contact");
            if (emergencyContact == null) {
                return;
            }

            double initialBalance = 0.0;

            // Create new tenant (NO CONTRACT - that goes in ContractManagement)
            Tenant newTenant = new Tenant(
                    contactNumber, firstName, lastName, password, userID,
                    username, User.Role.TENANT, tenantID, null, // roomID null initially
                    null, // contract null - will be created in ContractManagement
                    initialBalance, emergencyContact, idNumber);

            // Add to system (DatabaseManagement.addUser will add to in-memory list and
            // persist)
            DatabaseManagement.addUser(newTenant);

            System.out.println("\nTenant registered successfully!");
            System.out.println("Tenant ID: " + tenantID);
            System.out.println("Username: " + username);
            System.out.println("Note: Contract must be created separately in Contract Management");

        } catch (Exception e) {
            System.out.println("Error registering tenant: " + e.getMessage());
        }
    }

    public void assignRoom() {
        System.out.println("-------------------------------------------------");
        System.out.println("                 ASSIGN ROOM                     ");
        System.out.println("-------------------------------------------------");

        // Get tenants without rooms
        LinkedList<Tenant> tenantsWithoutRooms = new LinkedList<>();
        for (User user : User.getUsers()) {
            if (user instanceof Tenant) {
                Tenant tenant = (Tenant) user;
                if (tenant.getRoomID() == null) {
                    tenantsWithoutRooms.add(tenant);
                }
            }
        }

        if (tenantsWithoutRooms.isEmpty()) {
            System.out.println("No tenants available for room assignment (all tenants already have rooms).");
            return;
        }

        // Display tenants without rooms
        System.out.println("Tenants without rooms:");
        for (int i = 0; i < tenantsWithoutRooms.size(); i++) {
            Tenant tenant = tenantsWithoutRooms.get(i);
            System.out.println("[" + (i + 1) + "] " + tenant.getTenantID() + " - " + tenant.getFullName());
        }

        int tenantChoice = InputValidator.getValidInt(1, tenantsWithoutRooms.size(), "Select tenant");
        if (tenantChoice == -1) {
            return;
        }

        Tenant selectedTenant = tenantsWithoutRooms.get(tenantChoice - 1);

        // Get available rooms
        LinkedList<Room> availableRooms = new LinkedList<>();
        for (Room room : DatabaseManagement.getRooms()) {
            if (room.getStatus().toString().equals("Vacant")) {
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms for assignment.");
            return;
        }

        // Display available rooms
        System.out.println("Available rooms:");
        for (int i = 0; i < availableRooms.size(); i++) {
            Room room = availableRooms.get(i);
            System.out.println("[" + (i + 1) + "] " + room.getRoomID() + " - " + room.getRoomNumber()
                    + " (" + room.getType() + ") - ₱" + String.format("%.2f", room.getPrice()));
        }

        int roomChoice = InputValidator.getValidInt(1, availableRooms.size(), "Select room");
        if (roomChoice == -1) {
            return;
        }

        Room selectedRoom = availableRooms.get(roomChoice - 1);

        // Assign room to tenant
        selectedTenant.setRoomID(selectedRoom.getRoomID());
        selectedRoom.setStatus(RoomStatus.Occupied);

        // Save changes
        DatabaseManagement.saveUsers();
        DatabaseManagement.saveRooms(DatabaseManagement.getRooms());

        System.out.println("Room " + selectedRoom.getRoomNumber() + " assigned successfully to "
                + selectedTenant.getFullName());
        System.out.println("Note: Contract must be created separately in Contract Management");
    }

    public void removeTenant() {
        System.out.println("-------------------------------------------------");
        System.out.println("                 REMOVE TENANT                   ");
        System.out.println("-------------------------------------------------");

        LinkedList<Tenant> tenants = getTenants();
        if (tenants.isEmpty()) {
            System.out.println("No tenants to remove.");
            return;
        }

        // Display all tenants
        System.out.println("Select tenant to remove:");
        for (int i = 0; i < tenants.size(); i++) {
            Tenant tenant = tenants.get(i);
            System.out.println("[" + (i + 1) + "] " + tenant.getTenantID() + " - " + tenant.getFullName()
                    + " - Balance: ₱" + String.format("%.2f", tenant.getBalance()));
        }

        int tenantChoice = InputValidator.getValidInt(1, tenants.size(), "Select tenant");
        if (tenantChoice == -1) {
            return;
        }

        Tenant selectedTenant = tenants.get(tenantChoice - 1);

        // Check for pending balance
        if (selectedTenant.getBalance() > 0) {
            System.out.println("Cannot remove tenant with pending balance: ₱"
                    + String.format("%.2f", selectedTenant.getBalance()));
            Boolean forceRemove = InputValidator.getConfirmation("Force remove anyway?");
            if (forceRemove == null || !forceRemove) {
                return;
            }
        }

        // Free up the room if assigned
        if (selectedTenant.getRoomID() != null) {
            for (Room room : DatabaseManagement.getRooms()) {
                if (room.getRoomID().equals(selectedTenant.getRoomID())) {
                    room.setStatus(RoomStatus.Vacant);
                    break;
                }
            }
        }

        // Remove tenant from users list
        User.getUsers().remove(selectedTenant);

        // Save changes
        DatabaseManagement.saveUsers();
        DatabaseManagement.saveRooms(DatabaseManagement.getRooms());

        System.out.println("Tenant " + selectedTenant.getFullName() + " removed successfully.");
    }

    public void viewTenantRecords() {
        System.out.println("-------------------------------------------------");
        System.out.println("               TENANT RECORDS                    ");
        System.out.println("-------------------------------------------------");

        LinkedList<Tenant> tenants = getTenants();
        if (tenants.isEmpty()) {
            System.out.println("No tenant records found.");
            return;
        }

        for (Tenant tenant : tenants) {
            System.out.println("=== TENANT RECORD ===");
            System.out.println("Tenant ID:        " + tenant.getTenantID());
            System.out.println("Name:             " + tenant.getFullName());
            System.out.println("Username:         " + tenant.getUsername());
            System.out.println("Contact:          " + tenant.getContactNumber());
            System.out.println("ID Number:        " + tenant.getIdNumber());
            System.out.println("Emergency Contact: " + tenant.getEmergencyContact());
            System.out
                    .println("Room ID:          " + (tenant.getRoomID() != null ? tenant.getRoomID() : "Not assigned"));
            System.out.println("Balance:          ₱" + String.format("%.2f", tenant.getBalance()));
            System.out.println("=====================");
        }
    }

    // Helper method to get all tenants
    private LinkedList<Tenant> getTenants() {
        LinkedList<Tenant> tenants = new LinkedList<>();
        for (User user : User.getUsers()) {
            if (user instanceof Tenant) {
                tenants.add((Tenant) user);
            }
        }
        return tenants;
    }
}

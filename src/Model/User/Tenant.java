package Model.User;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

import Model.Billing.Bill;
import Model.Billing.Payment;
import Model.Contract.Contract;
import Model.Property.Room;
import Model.Request.MaintenanceRequest;
import Database.DatabaseManagement;

import java.util.Date;

public class Tenant extends User {

    private String tenantID;
    private String roomID;
    private Contract contract;
    private double balance;
    private String emergencyContact;
    

    public Tenant(String contactNumber, String firstName, String lastName, String password, String userID,
            String username, Role role,
            String tenantID, String roomID, Contract contract, double balance, String emergencyContact) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.tenantID = tenantID;
        this.roomID = roomID;
        this.contract = contract;
        this.balance = balance;
        this.emergencyContact = emergencyContact;
    }

    

    public static String generateNextTenantID() {
        LinkedList<User> users = User.getUsers();
        int maxNumber = 0;
        
        for (User user : users) {
            if (user instanceof Tenant) {
                Tenant tenant = (Tenant) user;
                String tenantID = tenant.getTenantID();
                if (tenantID.startsWith("TNT")) {
                    try {
                        int number = Integer.parseInt(tenantID.substring(3));
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        
        return "TNT" + String.format("%03d", maxNumber + 1);
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    @Override
    public void displayRoleMenu() {
        displayTenantMenu(new LinkedList<>(), null, new PriorityQueue<>());
    }

    public void displayTenantMenu(LinkedList<Bill> bills, Room room, PriorityQueue<MaintenanceRequest> maintenanceQueue) {
        try (Scanner sc = new Scanner(System.in)) {
            boolean exit;
            do {
                exit = false;
                System.out.println("=== TENANT MENU ===");
                System.out.println("1. My Profile");
                System.out.println("2. Edit Profile");
                System.out.println("3. My Contract");
                System.out.println("4. My Room");
                System.out.println("5. Billing and Payments");
                System.out.println("6. Submit Maintenance Request");
                System.out.println("7. View Maintenance Issues");
                System.out.println("8. Vote for Maintenance Request");
                System.out.println("9. Send Feedback / Complaint");
                System.out.println("10. Logout");
                System.out.print("\nChoose an option (1-10): ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        viewProfile();
                        break;
                    case 2:
                        System.out.print(
                                "Enter field to edit (firstname, lastname, contactnumber, username, password, emergencycontact): ");
                        String field = sc.nextLine();
                        System.out.print("Enter new value: ");
                        String newValue = sc.nextLine();
                        editProfile(field, newValue);
                        break;
                    case 3:
                        viewContract();
                        break;
                    case 4:
                        viewRoom(room);
                        break;
                    case 5:
                        viewBills(bills);
                        System.out.print("Do you want to make a payment? (yes/no): ");
                        if (sc.nextLine().equalsIgnoreCase("yes")) {
                            System.out.print("Enter Payment Amount: ");
                            double amount = Double.parseDouble(sc.nextLine());
                            Payment payment = new Payment("P" + System.currentTimeMillis(), amount, "Cash");
                            makePayment(payment);
                        }
                        break;
                    case 6:
                        System.out.print("Enter maintenance issue description: ");
                        String issue = sc.nextLine();
                        
                        MaintenanceRequest request = new MaintenanceRequest("MR" + System.currentTimeMillis(), null,
                                this, issue, new Date());
                        submitMaintenanceRequest(request);
                        break;
                    case 7:
                        viewMaintenanceIssues(maintenanceQueue);
                        break;
                    case 8:
                        System.out.print("Enter Maintenance Request ID to vote: ");
                        voteMaintenance(sc.nextLine(), maintenanceQueue);
                        break;
                    case 9:
                        System.out.print("Enter your feedback/complaint: ");
                        String feedback = sc.nextLine();
                        System.out.println("Feedback recorded: " + feedback);
                        break;
                    case 10:
                        logout();
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } while (!exit);
        }
    }

    @Override
    public void displayProfile() {
        System.out.println("-------------------------------------------------");
        System.out.println("                   TENANT PROFILE                ");
        System.out.println("-------------------------------------------------");
        System.out.println("Name:             " + getFirstName() + " " + getLastName());
        System.out.println("Username:         " + getUsername());
        System.out.println("Contact:          " + getContactNumber());
        System.out.println("Tenant ID:        " + getTenantID());
        System.out.println("Emergency Contact: " + getEmergencyContact());
        System.out.println("Room:             " + (getRoomID() != null ? getRoomID() : "Not assigned"));
        System.out.println("Balance:          ₱" + String.format("%.2f", getBalance()));
        
        
        if (contract != null) {
            System.out.println("Contract Status:  " + contract.getContractStatus());
            System.out.println("Monthly Rent:     ₱" + String.format("%.2f", contract.getMonthlyRent()));
            System.out.println("Start Date:       " + contract.getStartDate());
            System.out.println("End Date:         " + contract.getEndDate());
        } else {
            System.out.println("Contract Status:  No active contract");
        }
        
        System.out.println("-------------------------------------------------");
    }

    public void viewProfile() {
        displayProfile();
    }

    public void viewRoom(Room room) {
        System.out.println("===== ROOM DETAILS =====");
        if (room != null) {
            System.out.println("Room ID: " + room.getRoomID());
            System.out.println("Type: " + room.getType());
            System.out.println("Price: " + room.getPrice());
            System.out.println("Capacity: " + room.getCapacity());
            System.out.println("Amenities: " + room.getAmenities());
            System.out.println("Status: " + room.getStatus());
        } else {
            System.out.println("No room assigned.");
        }
        System.out.println("=========================");
    }

    public void viewContract() {
        if (contract != null) {
            System.out.println("===== CONTRACT DETAILS =====");
            System.out.println("Contract ID: " + contract.getContractID());
            System.out.println("Start Date: " + contract.getStartDate());
            System.out.println("End Date: " + contract.getEndDate());
            System.out.println("Monthly Rent: " + contract.getMonthlyRent());
            System.out.println("Status: " + contract.getContractStatus());
            System.out.println("============================");
        } else {
            System.out.println("No active contract found.");
        }
    }

    public void submitMaintenanceRequest(MaintenanceRequest request) {
        System.out.println("Maintenance request submitted successfully!");
    }

    public void viewBills(LinkedList<Bill> bills) {
        System.out.println("=== MY BILLS ===");
        for (Bill bill : bills) {
            if (bill.getTenantID().equals(this.tenantID)) {
                System.out.println("Bill ID: " + bill.getBillID());
                System.out.println("Amount: " + bill.getAmount());
                System.out.println("Type: " + bill.getType());
                System.out.println("Status: " + bill.getStatus());
                System.out.println("----------------------");
            }
        }
        System.out.println("Current Balance: " + balance);
    }

    public void makePayment(Payment payment) {
        balance -= payment.getAmount();
        System.out.println("Payment successful! Remaining balance: " + balance);
    }

    public void viewMaintenanceIssues(PriorityQueue<MaintenanceRequest> maintenanceQueue) {
        System.out.println("=== MAINTENANCE ISSUES ===");
        for (MaintenanceRequest request : maintenanceQueue) {
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Room: " + request.getRoomID().getRoomID());
            System.out.println("Reported by Tenant ID: " + request.getTenantID().getTenantID());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Status: " + request.getRequestStatus());
            System.out.println("Votes: " + request.getVoteCount());
            System.out.println("-------------------------");
        }
    }

    public void voteMaintenance(String requestID, PriorityQueue<MaintenanceRequest> maintenanceQueue) {
        for (MaintenanceRequest req : maintenanceQueue) {
            if (req.getRequestID().equals(requestID)) {
                req.addVote(this.tenantID);
                System.out.println("You voted for maintenance request: " + requestID);
                return;
            }
        }
        System.out.println("Maintenance request not found.");
    }

    public void logout() {
        System.out.println("Logging out...");
    }

    public void editProfile(String field, String newValue) {
        switch (field.toLowerCase()) {
            case "firstname" ->
                this.firstName = newValue;
            case "lastname" ->
                this.lastName = newValue;
            case "contactnumber" ->
                this.contactNumber = newValue;
            case "username" ->
                this.username = newValue;
            case "password" ->
                this.password = newValue;
            case "emergencycontact" ->
                this.emergencyContact = newValue;
            default ->
                System.out.println("Invalid field.");
        }
        System.out.println("Profile updated successfully!");
    }

    
    public Room getRoomDetails() {
        if (roomID != null) {
            for (Room room : DatabaseManagement.getRooms()) {
                if (room.getRoomID().equals(roomID)) {
                    return room;
                }
            }
        }
        return null;
    }
}
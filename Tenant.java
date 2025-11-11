import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Tenant extends User {
    private String tenantID;
    private String roomID;
    private Contract contract;
    private double balance;
    private String emergencyContact;

    public Tenant(String userID, String username, String password, String firstName, String lastName, String contactNumber, Role role, String tenantID, String roomID, Contract contract, double balance, String emergencyContact) {
        super(userID, username, password, firstName, lastName, contactNumber, role);
        this.tenantID = tenantID;
        this.roomID = roomID;
        this.contract = contract;
        this.balance = balance;
        this.emergencyContact = emergencyContact;
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

    public void displayTenantMenu(List<Bill> bills, Room room, PriorityQueue<MaintenanceRequest> maintenanceQueue, Admin admin) {
        Scanner sc = new Scanner(System.in);
        boolean exit;
        do {
            exit = false;
            System.out.println("=== TENANT MENU ===");
            System.out.println("1. My Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. My Room");
            System.out.println("4. Billing and Payments");
            System.out.println("5. Submit Maintenance Request");
            System.out.println("6. View Maintenance Issues");
            System.out.println("7. Vote for Maintenance Request");
            System.out.println("8. Send Feedback / Complaint");
            System.out.println("9. Logout");
            System.out.print("\nChoose an option (1-9): ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    System.out.print("Enter field to edit (firstname, lastname, contactnumber, username, password, emergencycontact): ");
                    String field = sc.nextLine();
                    System.out.print("Enter new value: ");
                    String newValue = sc.nextLine();
                    editProfile(field, newValue);
                    break;
                case 3:
                    viewRoom(room);
                    break;
                case 4:
                    viewBills(bills);
                    System.out.print("Do you want to make a payment? (yes/no): ");
                    if (sc.nextLine().equalsIgnoreCase("yes")) {
                        System.out.print("Enter Payment Amount: ");
                        double amount = Double.parseDouble(sc.nextLine());
                        Payment payment = new Payment("P" + System.currentTimeMillis(), amount, "Cash");
                        makePayment(payment);
                    }
                    break;
                case 5:
                    System.out.print("Enter maintenance issue description: ");
                    String issue = sc.nextLine();
                    MaintenanceRequest request = new MaintenanceRequest("MR" + System.currentTimeMillis(), roomID, tenantID, issue);
                    submitMaintenanceRequest(request);
                    break;
                case 6:
                    viewMaintenanceIssues(maintenanceQueue);
                    break;
                case 7:
                    System.out.print("Enter Maintenance Request ID to vote: ");
                    voteMaintenance(sc.nextLine(), maintenanceQueue);
                    break;
                case 8:
                    System.out.print("Enter your feedback/complaint: ");
                    sendFeedback(sc.nextLine(), admin);
                    break;
                case 9:
                    logout();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (!exit);
    }

    //add sa uml
    public void viewProfile() {
        System.out.println("=== MY PROFILE ===");
        System.out.println("Tenant ID: " + getTenantID());
        System.out.println("Name: " + getFirstName() + " " + getLastName());
        System.out.println("Contact Number: " + getContactNumber());
        System.out.println("Username: " + getUsername());
        System.out.println("Emergency Contact: " + getEmergencyContact());
        System.out.println("==================");
    }

    public void viewRoom(Room room) {
        System.out.println("===== ROOM DETAILS =====");
        System.out.println("Room ID: " + room.getRoomID());
        System.out.println("Type: " + room.getRoomType());
        System.out.println("Price: " + room.getPrice());
        System.out.println("Capacity: " + room.getCapacity());
        System.out.println("Amenities: " + room.getAmenities());
        System.out.println("Status: " + room.getRoomStatus());
        System.out.println("=========================");
    }

    public void viewContract() {
        if (contract != null) {
            System.out.println("===== CONTRACT DETAILS =====");
            System.out.println("Contract ID: " + contract.getContractID());
            System.out.println("Start Date: " + contract.getStartDate());
            System.out.println("End Date: " + contract.getEndDate());
            System.out.println("Monthly Rent: " + contract.getMonthlyRent());
            System.out.println("Status: " + contract.getStatus());
            System.out.println("============================");
        } else {
            System.out.println("No active contract found.");
        }
    }

    public void submitMaintenanceRequest(MaintenanceRequest request) {
        request.setStatus("Pending");
        System.out.println("Maintenance request submitted successfully!");
    }

    public void viewBills(List<Bill> bills) {
        System.out.println("=== MY BILLS ===");
        for (Bill bill : bills) {
            if (bill.getTenantID().equals(this.tenantID)) {
                System.out.println("Bill ID: " + bill.getBillID());
                System.out.println("Amount: " + bill.getAmount());
                System.out.println("Due Date: " + bill.getDueDate());
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
            System.out.println("Room: " + request.getRoomID());
            System.out.println("Reported by Tenant ID: " + request.getTenantID());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Status: " + request.getStatus());
            System.out.println("Votes: " + request.getVotes());
            System.out.println("-------------------------");
        }
    }

    public void voteMaintenance(String requestID, PriorityQueue<MaintenanceRequest> maintenanceQueue) {
        for (MaintenanceRequest req : maintenanceQueue) {
            if (req.getRequestID().equals(requestID)) {
                req.addVote();
                System.out.println("You voted for maintenance request: " + requestID);
                return;
            }
        }
        System.out.println("Maintenance request not found.");
    }

    public void sendFeedback(String message, Admin admin) {
        admin.receiveFeedback(this, message);
        System.out.println("Feedback sent successfully!");
    }

    public void logout() {
        System.out.println("Logging out...");
    }
}

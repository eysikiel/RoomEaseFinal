package Managements;

import Utilities.InputValidator;
import Model.User.User;
import Model.User.Tenant;
import Model.Property.Room;
import Model.Contract.Contract;
import Database.DatabaseManagement;

import java.time.LocalDate;
import java.util.LinkedList;

public class ContractManagement {
    private DatabaseManagement databaseManager;
    private LinkedList<Contract> contractHistory;

    public ContractManagement(DatabaseManagement databaseManager) {
        this.databaseManager = databaseManager;
        this.contractHistory = new LinkedList<>();
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("              CONTRACT MANAGEMENT                ");
            System.out.println("-------------------------------------------------");
            System.out.println("[1] View All Contracts");
            System.out.println("[2] Create New Contract");
            System.out.println("[3] Renew Contract");
            System.out.println("[4] Terminate Contract");
            System.out.println("[5] View Contract History");
            System.out.println("[6] Back to Main Menu");
            System.out.println("-------------------------------------------------");
            
            choice = InputValidator.getMenuChoice(6);
            if (choice == -1) continue;

            switch (choice) {
                case 1:
                    viewAllContracts();
                    break;
                case 2:
                    createNewContract();
                    break;
                case 3:
                    renewContract();
                    break;
                case 4:
                    terminateContract();
                    break;
                case 5:
                    viewContractHistory();
                    break;
                case 6:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    public void viewAllContracts() {
        System.out.println("-------------------------------------------------");
        System.out.println("               VIEW ALL CONTRACTS                ");
        System.out.println("-------------------------------------------------");
        
        LinkedList<Contract> activeContracts = getActiveContracts();
        if (activeContracts.isEmpty()) {
            System.out.println("No active contracts found.");
            return;
        }

        for (Contract contract : activeContracts) {
            Tenant tenant = findTenantByContract(contract);
            System.out.println("Contract ID:   " + contract.getContractID());
            System.out.println("Tenant:        " + (tenant != null ? tenant.getFullName() : "Unknown"));
            System.out.println("Room:          " + (tenant != null && tenant.getRoomID() != null ? tenant.getRoomID() : "Not assigned"));
            System.out.println("Start Date:    " + contract.getStartDate());
            System.out.println("End Date:      " + contract.getEndDate());
            System.out.println("Monthly Rent:  ₱" + String.format("%.2f", contract.getMonthlyRent()));
            System.out.println("Status:        " + contract.getContractStatus());
            System.out.println("-------------------------------------------------");
        }
    }

    public void createNewContract() {
        System.out.println("-------------------------------------------------");
        System.out.println("              CREATE NEW CONTRACT                ");
        System.out.println("-------------------------------------------------");
        
        // Get tenants without active contracts
        LinkedList<Tenant> availableTenants = new LinkedList<>();
        for (User user : User.getUsers()) {
            if (user instanceof Tenant) {
                Tenant tenant = (Tenant) user;
                if (tenant.getContract() == null && tenant.getRoomID() != null) {
                    availableTenants.add(tenant);
                }
            }
        }

        if (availableTenants.isEmpty()) {
            System.out.println("No tenants available for new contract (all tenants have contracts or no room assigned).");
            return;
        }

        // Display available tenants
        System.out.println("Available Tenants (with assigned rooms):");
        for (int i = 0; i < availableTenants.size(); i++) {
            Tenant tenant = availableTenants.get(i);
            System.out.println("[" + (i + 1) + "] " + tenant.getTenantID() + " - " + tenant.getFullName() + 
                             " (Room: " + tenant.getRoomID() + ")");
        }

        int tenantChoice = InputValidator.getValidInt(1, availableTenants.size(), "Select tenant");
        if (tenantChoice == -1) return;

        Tenant selectedTenant = availableTenants.get(tenantChoice - 1);

        // Get contract details
        double monthlyRent = InputValidator.getValidDouble(1, 100000, "Enter monthly rent");
        if (monthlyRent == -1) return;

        double deposit = InputValidator.getValidDouble(0, 50000, "Enter security deposit");
        if (deposit == -1) return;

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startDateStr = InputValidator.scanner.nextLine();
        LocalDate startDate = LocalDate.parse(startDateStr);

        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endDateStr = InputValidator.scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateStr);

        // Create contract
        String contractID = "CNT" + System.currentTimeMillis();
        Contract newContract = new Contract(contractID, startDate, endDate, monthlyRent);
        newContract.setSecurityDeposit(deposit);
        newContract.setContractStatus("Active");

        // Assign contract to tenant
        selectedTenant.setContract(newContract);

        // Update tenant balance with deposit
        selectedTenant.setBalance(selectedTenant.getBalance() + deposit);

        // Save changes
        DatabaseManagement.saveUsers();

        System.out.println("\nContract created successfully!");
        System.out.println("Contract ID: " + contractID);
        System.out.println("Tenant: " + selectedTenant.getFullName());
        System.out.println("Monthly Rent: ₱" + String.format("%.2f", monthlyRent));
        System.out.println("Security Deposit: ₱" + String.format("%.2f", deposit));
    }

    public void renewContract() {
        System.out.println("-------------------------------------------------");
        System.out.println("                RENEW CONTRACT                   ");
        System.out.println("-------------------------------------------------");
        
        LinkedList<Contract> activeContracts = getActiveContracts();
        if (activeContracts.isEmpty()) {
            System.out.println("No active contracts to renew.");
            return;
        }

        // Display active contracts
        System.out.println("Active Contracts:");
        for (int i = 0; i < activeContracts.size(); i++) {
            Contract contract = activeContracts.get(i);
            Tenant tenant = findTenantByContract(contract);
            System.out.println("[" + (i + 1) + "] " + contract.getContractID() + " - " + 
                             (tenant != null ? tenant.getFullName() : "Unknown") + 
                             " (Ends: " + contract.getEndDate() + ")");
        }

        int contractChoice = InputValidator.getValidInt(1, activeContracts.size(), "Select contract to renew");
        if (contractChoice == -1) return;

        Contract selectedContract = activeContracts.get(contractChoice - 1);

        // Get new end date
        System.out.print("Enter new End Date (YYYY-MM-DD): ");
        String newEndDateStr = InputValidator.scanner.nextLine();
        LocalDate newEndDate = LocalDate.parse(newEndDateStr);

        // Ask about rent adjustment
        Boolean adjustRent = InputValidator.getConfirmation("Do you want to adjust the monthly rent?");
        if (adjustRent != null && adjustRent) {
            double newRent = InputValidator.getValidDouble(1, 100000, "Enter new monthly rent");
            if (newRent != -1) {
                selectedContract.setMonthlyRent(newRent);
            }
        }

        // Renew contract
        selectedContract.setEndDate(newEndDate);

        // Save changes
        DatabaseManagement.saveUsers();

        System.out.println("Contract renewed successfully!");
        System.out.println("New end date: " + newEndDate);
        System.out.println("Monthly rent: ₱" + String.format("%.2f", selectedContract.getMonthlyRent()));
    }

    public void terminateContract() {
        System.out.println("-------------------------------------------------");
        System.out.println("              TERMINATE CONTRACT                 ");
        System.out.println("-------------------------------------------------");
        
        LinkedList<Contract> activeContracts = getActiveContracts();
        if (activeContracts.isEmpty()) {
            System.out.println("No active contracts to terminate.");
            return;
        }

        // Display active contracts
        System.out.println("Active Contracts:");
        for (int i = 0; i < activeContracts.size(); i++) {
            Contract contract = activeContracts.get(i);
            Tenant tenant = findTenantByContract(contract);
            System.out.println("[" + (i + 1) + "] " + contract.getContractID() + " - " + 
                             (tenant != null ? tenant.getFullName() : "Unknown"));
        }

        int contractChoice = InputValidator.getValidInt(1, activeContracts.size(), "Select contract to terminate");
        if (contractChoice == -1) return;

        Contract selectedContract = activeContracts.get(contractChoice - 1);
        Tenant tenant = findTenantByContract(selectedContract);

        // Get termination reason
        String reason = InputValidator.getNonEmptyString("Enter termination reason");
        if (reason == null) return;

        // Terminate contract
        selectedContract.setContractStatus("Terminated");
        selectedContract.setTerminationReason(reason);

        // Free up room if tenant has one
        if (tenant != null && tenant.getRoomID() != null) {
            for (Room room : DatabaseManagement.getRooms()) {
                if (room.getRoomID().equals(tenant.getRoomID())) {
                    room.setStatus(Model.Property.Room.RoomStatus.Vacant);
                    break;
                }
            }
            tenant.setRoomID(null);
        }

        // Move to history
        contractHistory.add(selectedContract);
        if (tenant != null) {
            tenant.setContract(null);
        }

        // Save changes
        DatabaseManagement.saveUsers();
        DatabaseManagement.saveRooms(DatabaseManagement.getRooms());

        System.out.println("Contract terminated successfully!");
        System.out.println("Reason: " + reason);
    }

    public void viewContractHistory() {
        System.out.println("-------------------------------------------------");
        System.out.println("              CONTRACT HISTORY                   ");
        System.out.println("-------------------------------------------------");
        
        if (contractHistory.isEmpty()) {
            System.out.println("No contract history found.");
            return;
        }

        for (Contract contract : contractHistory) {
            System.out.println("Contract ID:   " + contract.getContractID());
            System.out.println("Start Date:    " + contract.getStartDate());
            System.out.println("End Date:      " + contract.getEndDate());
            System.out.println("Monthly Rent:  ₱" + String.format("%.2f", contract.getMonthlyRent()));
            System.out.println("Status:        " + contract.getContractStatus());
            if (contract.getTerminationReason() != null) {
                System.out.println("Term Reason:   " + contract.getTerminationReason());
            }
            System.out.println("-------------------------------------------------");
        }
    }

    // Helper methods
    private LinkedList<Contract> getActiveContracts() {
        LinkedList<Contract> activeContracts = new LinkedList<>();
        for (User user : User.getUsers()) {
            if (user instanceof Tenant) {
                Tenant tenant = (Tenant) user;
                if (tenant.getContract() != null && 
                    tenant.getContract().getContractStatus().equals("Active")) {
                    activeContracts.add(tenant.getContract());
                }
            }
        }
        return activeContracts;
    }

    private Tenant findTenantByContract(Contract contract) {
        for (User user : User.getUsers()) {
            if (user instanceof Tenant) {
                Tenant tenant = (Tenant) user;
                if (tenant.getContract() == contract) {
                    return tenant;
                }
            }
        }
        return null;
    }
}
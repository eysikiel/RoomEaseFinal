package Managements;

import Utilities.InputValidator;
import Model.User.User;
import Model.User.Tenant;
import Model.Property.Room;
import Model.Contract.Contract;
import Database.DatabaseManagement;

import java.time.LocalDate;
import java.util.Date;
import Enums.ContractStatus;
import Enums.RoomStatus;
import java.util.LinkedList;

public class ContractManagement {
    // DatabaseManagement is static-style; no instance required here
    private LinkedList<Contract> contractHistory;
    // in-memory list of all persisted contracts
    private LinkedList<Contract> contracts;

    public ContractManagement() {
        this.contractHistory = new LinkedList<>();
        // load persisted contracts and initialize history
        this.contracts = DatabaseManagement.getContracts();
        for (Contract c : this.contracts) {
            if (c.getContractStatus() != Enums.ContractStatus.Active) {
                this.contractHistory.add(c);
            } else {
                // attach active contract to tenant in-memory if not already
                Tenant t = c.getTenantID();
                if (t != null && t.getContract() == null) {
                    t.setContract(c);
                }
            }
        }
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
            if (choice == -1)
                continue;

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
            System.out.println("Room:          "
                    + (tenant != null && tenant.getRoomID() != null ? tenant.getRoomID() : "Not assigned"));
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
            System.out
                    .println("No tenants available for new contract (all tenants have contracts or no room assigned).");
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
        if (tenantChoice == -1)
            return;

        Tenant selectedTenant = availableTenants.get(tenantChoice - 1);

        // Get contract details
        double monthlyRent = InputValidator.getValidDouble(1, 100000, "Enter monthly rent");
        if (monthlyRent == -1)
            return;

        double deposit = InputValidator.getValidDouble(0, 50000, "Enter security deposit");
        if (deposit == -1)
            return;

        LocalDate startDate = InputValidator.getValidDate("Enter Start Date");
        if (startDate == null)
            return;

        LocalDate endDate = InputValidator.getValidDate("Enter End Date");
        if (endDate == null)
            return;

        // Convert to java.util.Date
        Date start = java.sql.Date.valueOf(startDate);
        Date end = java.sql.Date.valueOf(endDate);

        // basic date validation
        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            System.out.println("End date must be after start date.");
            return;
        }

        // Create contract using tenant and room objects
        String contractID = "CNT" + System.currentTimeMillis();
        // find the Room object for tenant
        Room tenantRoom = null;
        if (selectedTenant.getRoomID() != null) {
            for (Room r : DatabaseManagement.getRooms()) {
                if (r.getRoomID().equals(selectedTenant.getRoomID())) {
                    tenantRoom = r;
                    break;
                }
            }
        }

        // extra validation: ensure room is vacant
        if (tenantRoom != null && tenantRoom.getStatus() == RoomStatus.Occupied) {
            System.out.println("Selected room is not vacant. Cannot create contract.");
            return;
        }

        Contract newContract = new Contract(contractID, selectedTenant, tenantRoom, start, end, monthlyRent,
                deposit, ContractStatus.Active);

        // Assign contract to tenant
        selectedTenant.setContract(newContract);

        // Update tenant balance with deposit
        selectedTenant.setBalance(selectedTenant.getBalance() + deposit);

        // Mark room as occupied
        if (tenantRoom != null) {
            tenantRoom.setStatus(RoomStatus.Occupied);
            DatabaseManagement.saveRooms(DatabaseManagement.getRooms());
        }

        // persist contract to disk
        LinkedList<Contract> persisted = DatabaseManagement.getContracts();
        persisted.add(newContract);
        DatabaseManagement.saveContracts(persisted);

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
        if (contractChoice == -1)
            return;

        Contract selectedContract = activeContracts.get(contractChoice - 1);

        // Ask about rent adjustment
        Boolean doAdjustRent = InputValidator.getConfirmation("Do you want to adjust the monthly rent?");
        if (doAdjustRent != null && doAdjustRent) {
            double newRent = InputValidator.getValidDouble(1, 100000, "Enter new monthly rent");
            if (newRent != -1) {
                selectedContract.setMonthlyRent(newRent);
            }
        }

        LocalDate newEndDate = InputValidator.getValidDate("Enter new End Date");
        if (newEndDate == null)
            return;
        // validate new end date must be after current
        java.util.Date existingEnd = selectedContract.getEndDate();
        if (existingEnd != null && !java.sql.Date.valueOf(newEndDate).after(existingEnd)) {
            System.out.println("New end date must be after the current end date.");
            return;
        }
        selectedContract.setEndDate(java.sql.Date.valueOf(newEndDate));

        // Save changes
        // update persistent store
        LinkedList<Contract> persistedRenew = DatabaseManagement.getContracts();
        for (int i = 0; i < persistedRenew.size(); i++) {
            if (persistedRenew.get(i).getContractID().equals(selectedContract.getContractID())) {
                persistedRenew.set(i, selectedContract);
                break;
            }
        }
        DatabaseManagement.saveContracts(persistedRenew);
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
        if (contractChoice == -1)
            return;

        Contract selectedContract = activeContracts.get(contractChoice - 1);
        Tenant tenant = findTenantByContract(selectedContract);

        // Get termination reason
        String reason = InputValidator.getNonEmptyString("Enter termination reason");
        if (reason == null)
            return;

        // Terminate contract
        selectedContract.setContractStatus(ContractStatus.Terminated);
        selectedContract.setTerminationReason(reason);

        // Free up room if tenant has one
        if (tenant != null && tenant.getRoomID() != null) {
            for (Room room : DatabaseManagement.getRooms()) {
                if (room.getRoomID().equals(tenant.getRoomID())) {
                    room.setStatus(RoomStatus.Vacant);
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

        // persist updated contract list
        LinkedList<Contract> persisted = DatabaseManagement.getContracts();
        for (int i = 0; i < persisted.size(); i++) {
            if (persisted.get(i).getContractID().equals(selectedContract.getContractID())) {
                persisted.set(i, selectedContract);
                break;
            }
        }
        DatabaseManagement.saveContracts(persisted);

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
                        tenant.getContract().getContractStatus() == ContractStatus.Active) {
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
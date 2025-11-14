import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Contract {
    private String contractID;
    private Tenant tenantID;
    private Room roomID;
    private Date startDate;
    private Date endDate;
    private double monthlyRent;
    private double deposit;
    private ContractStatus contractStatus;
    Scanner scanner = new Scanner(System.in);

    public Contract(String contractID, Tenant tenantID, Room roomID, Date startDate, Date endDate, double monthlyRent, double deposit, ContractStatus contractStatus) {
        this.contractID = contractID;
        this.tenantID = tenantID;
        this.roomID = roomID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyRent = monthlyRent;
        this.deposit = deposit;
        this.contractStatus = contractStatus;
    }

    public String getContractID() {
        return contractID;
    }

    public Tenant getTenantID() {
        return tenantID;
    }

    public Room getRoomID() {
        return roomID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public double getDeposit() {
        return deposit;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public void setTenantID(Tenant tenantID) {
        this.tenantID = tenantID;
    }

    public void setRoomID(Room roomID) {
        this.roomID = roomID;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMonthlyRent(double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public void activate() {
        if (contractStatus == ContractStatus.Active) {
            System.out.println("Contract is already active!");
        } else if (contractStatus == ContractStatus.Terminated) {
            System.out.println("Cannot activate a Terminated contract.");
        } else {
            contractStatus = ContractStatus.Active;
            System.out.println("Contract " + this.getContractID() + " has been ACTIVATED.");
        }
    }

    public void terminate(String reason, Scanner scanner) {
        if (contractStatus == ContractStatus.Terminated) {
            System.out.println("Contract already terminated.");
        } else if (contractStatus == ContractStatus.Expired) {
            System.out.println("Contract already expired.");
        } else {
            System.out.println("Please attach reason for termination: ");
            reason = scanner.next();

            contractStatus = ContractStatus.Terminated;
            System.out.println("Contract " + this.getContractID() + " has been terminated.");
            System.out.println("Reason of termination: " + reason);
        }
    }

    public void renew(Date newEndDate) {
        if (contractStatus == ContractStatus.Terminated) {
            System.out.println("Cannot renew a terminated contract.");
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.endDate);
            calendar.add(Calendar.YEAR, 1);
            newEndDate = calendar.getTime();

            this.endDate = newEndDate;
            contractStatus = ContractStatus.PendingRenewal;

            System.out.println("Contract " + contractID + " has added to Renewal Queue.");
            System.out.println("Expected new end date: " + newEndDate);
        }
    }

    public void displayContract() {
        System.out.println("========== CONTRACT DETAILS ==========");
        System.out.println("Contract ID: " + contractID);

        if (tenantID != null) {
            System.out.println("Tenant: " + tenantID.getFirstName() + " " + tenantID.getLastName());
            System.out.println("Tenant ID: " + tenantID.getTenantID());
        } else {
            System.out.println("Tenant: None assigned");
        }

        if (roomID != null) {
            System.out.println("Room: " + roomID.getRoomID());
            System.out.println("Room Type: " + roomID.getRoomType());
        } else {
            System.out.println("Room: None assigned");
        }

        System.out.println("Start Date: " + this.getStartDate());
        System.out.println("End Date: " + this.getEndDate());
        System.out.println("Monthly Rent Amount: " + this.getMonthlyRent());
        System.out.println("Deposit: " + this.getDeposit());
        System.out.println("Status: " + this.getContractStatus());
        System.out.println("======================================");
    }

    public void markPendingTermination() {
        if (contractStatus == ContractStatus.PendingTermination) {
            System.out.println("Contract is already in line for termination.");
        } else {
            contractStatus = ContractStatus.PendingTermination;
            System.out.println("Contract " + contractID + " is now marked for PENDING TERMINATION.");
        }
    }

    public void markPendingRenewal() {
        if (contractStatus == ContractStatus.PendingRenewal) {
            System.out.println("Contract is already in line for termination.");
        } else {
            contractStatus = ContractStatus.PendingRenewal;
            System.out.println("Contract " + contractID + " is now marked for PENDING RENEWAL.");
        }
    }

}

package Model.Request;

import java.util.Date;
import java.util.Scanner;

import Enums.RequestStatus;
import Model.Contract.Contract;
import Model.Property.Room;

public class TerminationRequest extends Requests {
    private Contract contractID;
    private Date terminationDate;
    private String terminationReason;

    public TerminationRequest(String requestID, Room roomID, RequestStatus requestStatus, Contract contractID,
            Date terminationDate, String terminationReason) {
        super(requestID, roomID, requestStatus);
        this.contractID = contractID;
        this.terminationDate = terminationDate;
        this.terminationReason = terminationReason;
    }

    public Contract getContractID() {
        return contractID;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public void setContractID(Contract contractID) {
        this.contractID = contractID;
    }

    public void setTerminationDate(Date terminationDate) {
        this.terminationDate = terminationDate;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    @Override
    public void displayRequest() {
        System.out.println("=== Termination Request ===");
        System.out.println("Request ID: " + this.getRequestID());
        System.out.println("Room: " + this.getRoomID());
        System.out.println("Contract ID: " + this.getContractID().getContractID());
        System.out.println("Tenant: " + this.getContractID().getTenantID().getFullName());
        System.out.println("Termination Date: " + this.getTerminationDate());
        System.out.println("Termination Reason: " + this.getTerminationReason());
        System.out.println("Original End Date: " + this.getContractID().getEndDate());
        System.out.println("Monthly Rent: P" + this.getContractID().getMonthlyRent());
        System.out.println("Deposit Amount: P" + this.getContractID().getDeposit());
        System.out.println("Refund Amount: P" + this.calculateRefund());
        System.out.println("Contract Status: " + this.getContractID().getContractStatus());
        System.out.println("Request Status: " + getRequestStatus());
    }

    public void approveTermination() {
        if (getRequestStatus() == RequestStatus.APPROVED) {
            Scanner scanner = new Scanner(System.in);
            contractID.terminate(terminationReason, scanner);
            System.out.println("Termination approved for contractID " + contractID.getContractID());
        }
    }

    public boolean isEarlyTermination() {
        return terminationDate.before(contractID.getEndDate());
    }

    public double calculateRefund() {
        if (isEarlyTermination()) {
            return Math.max(0, contractID.getDeposit() - contractID.getMonthlyRent());
        } else {
            return contractID.getDeposit();
        }
    }
}
package Model.Request;

import java.util.Date;

import Enums.ContractStatus;
import Enums.RequestStatus;
import Model.Contract.Contract;
import Model.Property.Room;

public class RenewalRequest extends Requests {
    private Contract contractID;
    private Date proposedEndDate;

    public RenewalRequest(String requestID, Room roomID, RequestStatus requestStatus, Contract contractID,
            Date proposedEndDate) {
        super(requestID, roomID, requestStatus);
        this.contractID = contractID;
        this.proposedEndDate = proposedEndDate;
    }

    public Contract getContractID() {
        return contractID;
    }

    public Date getProposedEndDate() {
        return proposedEndDate;
    }

    public void setContractID(Contract contractID) {
        this.contractID = contractID;
    }

    public void setProposedEndDate(Date proposedEndDate) {
        this.proposedEndDate = proposedEndDate;
    }

    @Override
    public void displayRequest() {
        System.out.println("=== Renewal Request ===");
        System.out.println("Request ID: " + this.getRequestID());
        System.out.println("Room: " + this.getRoomID());
        System.out.println("Contract ID: " + this.getContractID().getContractID());
        System.out.println("Current Tenant: " + this.getContractID().getTenantID().getFullName());
        System.out.println("Current End Date: " + this.getContractID().getEndDate());
        System.out.println("Proposed End Date: " + this.getProposedEndDate());
        System.out.println("Monthly Rent: P" + this.getContractID().getMonthlyRent());
        System.out.println("Contract Status: " + this.getContractID().getContractStatus());
        System.out.println("Request Status: " + this.getRequestStatus());
    }

    public void approveRenewal() {
        if (getRequestStatus() == RequestStatus.APPROVED) {
            this.contractID.renew(proposedEndDate);
            System.out.println("Renewal approved for contract " + this.getContractID().getContractID());
        }
    }

    public void rejectRenewal() {
        if (getRequestStatus() == RequestStatus.REJECTED) {
            this.contractID.setEndDate(proposedEndDate);
            this.contractID.setContractStatus(ContractStatus.PendingRenewal);
            System.out.println("Renewal rejected. Please contact your Landlord for further actions.");
        }
    }
}
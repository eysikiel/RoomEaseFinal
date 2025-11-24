package Model.Request;
import java.util.ArrayList;
import java.util.Date;

import Enums.RequestStatus;
import Model.Property.Room;
import Model.User.Tenant;

public class MaintenanceRequest extends Requests {
    private Tenant tenantID;
    private String description;
    private Date dateReported;
    private ArrayList<String> voters;

    public MaintenanceRequest(String requestID, Room roomID, Tenant tenantID, String description, Date dateReported) {
        super(requestID, roomID, RequestStatus.PENDING);
        this.tenantID = tenantID;
        this.description = description;
        this.dateReported = dateReported;
        this.voters = new ArrayList<>();
        this.voters.add(tenantID.getTenantID());
    }

    public Tenant getTenantID() {
        return tenantID;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setTenantID(Tenant tenantID) {
        this.tenantID = tenantID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    public void setVoters(ArrayList<String> voters) {
        this.voters = voters;
    }

    public boolean addVote(String tenantID) {
        if (!voters.contains(tenantID)) {
            voters.add(tenantID);
            return true;
        }
        return false;
    }

    public int getVoteCount() {
        return voters.size();
    }

    public ArrayList<String> getVoters() {
        return voters;
    }

    @Override
    public void displayRequest() {
        System.out.println("=== Maintenance Request ===");
        System.out.println("Request ID: " + this.getRequestID());
        System.out.println("Tenant: " + this.tenantID.getFullName());
        System.out.println("Room: " + this.getRoomID().getRoomID());
        System.out.println("Description: " + this.getDescription());
        System.out.println("Date Reported: " + this.getDateReported());
        System.out.println("Votes: " + this.getVoteCount());
        System.out.println("Voters: " + this.getVoters());
        System.out.println("Status: " + this.getRequestStatus());
    }
}

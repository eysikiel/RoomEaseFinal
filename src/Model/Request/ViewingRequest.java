package Model.Request;

import java.util.Date;

import Enums.RequestStatus;
import Model.Property.Room;
import Model.User.Applicant;

public class ViewingRequest extends Requests {
    private Applicant applicantID;
    private Date scheduledDate;

    public ViewingRequest(String requestID, Room roomID, RequestStatus requestStatus, Applicant applicantID,
            Date scheduledDate) {
        super(requestID, roomID, requestStatus);
        this.applicantID = applicantID;
        this.scheduledDate = scheduledDate;
    }

    public Applicant getApplicantID() {
        return applicantID;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setApplicantID(Applicant applicantID) {
        this.applicantID = applicantID;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Override
    public void displayRequest() {
        System.out.println("=== Viewing Request ===");
        System.out.println("Request ID: " + this.getRequestID());
        System.out.println("Applicant: " + this.getApplicantID().getFullName());
        System.out.println("Room: " + this.getRoomID().getRoomID());
        System.out.println("Date Scheduled: " + this.getScheduledDate());
        System.out.println("Status: " + this.getRequestStatus());
    }
}

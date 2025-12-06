package Model.Request;

import Model.User.Applicant;
import Model.Property.Room;
import Enums.RequestStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data model for a Viewing Request.
 * This class provides the fields and simple accessors/mutators used by
 * management and database components.
 */
public class ViewingRequest {
    private String requestID;
    private Room roomID;
    private RequestStatus requestStatus;
    private Applicant applicantID;
    private Date scheduledDate;

    private static final SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ViewingRequest(String requestID, Room roomID, RequestStatus requestStatus, Applicant applicantID,
            Date scheduledDate) {
        this.requestID = requestID;
        this.roomID = roomID;
        this.requestStatus = requestStatus;
        this.applicantID = applicantID;
        this.scheduledDate = scheduledDate;
    }

    public String getRequestID() {
        return requestID;
    }

    public Room getRoomID() {
        return roomID;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public Applicant getApplicantID() {
        return applicantID;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setRequestStatus(RequestStatus status) {
        this.requestStatus = status;
    }

    public void setScheduledDate(Date d) {
        this.scheduledDate = d;
    }

    /**
     * Display a human-readable representation of the viewing request.
     * Management/UI classes call this for simple console output.
     */
    public void displayRequest() {
        System.out.println("Request ID: " + requestID);
        System.out.println("Room: " + (roomID != null ? roomID.getRoomNumber() : "N/A"));
        System.out.println("Applicant: " + (applicantID != null ? applicantID.getFullName() : "N/A"));
        System.out.println("Scheduled: " + (scheduledDate != null ? displayFormat.format(scheduledDate) : "N/A"));
        System.out.println("Status: " + (requestStatus != null ? requestStatus.name() : "N/A"));
    }
}
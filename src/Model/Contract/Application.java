package Model.Contract;

import java.util.Date;

public class Application {
    private String applicationID;
    private String applicantID;
    private String preferredRoomID;
    private String applicationStatus;
    private Date dateSubmitted;
    private boolean initialPaymentMade;

    public Application(String applicationID, String applicantID, String preferredRoomID, String applicationStatus,
            Date dateSubmitted, boolean initialPaymentMade) {
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.preferredRoomID = preferredRoomID;
        this.applicationStatus = applicationStatus;
        this.dateSubmitted = dateSubmitted;
        this.initialPaymentMade = initialPaymentMade;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getApplicantID() {
        return applicantID;
    }

    public String getPreferredRoomID() {
        return preferredRoomID;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public boolean isInitialPaymentMade() {
        return initialPaymentMade;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setPreferredRoomID(String preferredRoomID) {
        this.preferredRoomID = preferredRoomID;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setInitialPaymentMade(boolean initialPaymentMade) {
        this.initialPaymentMade = initialPaymentMade;
    }

    public String checkStatus() {
        if (applicationStatus == null) {
            return "No status available";
        }

        switch (applicationStatus.toLowerCase()) {
            case "approved":
                return initialPaymentMade
                        ? "Approved - Ready for move-in"
                        : "Approved - Waiting for initial payment";

            case "pending":
                return initialPaymentMade
                        ? "Pending - Under review"
                        : "Pending - Waiting for initial payment";

            case "rejected":
                return "Rejected";

            default:
                return "Unknown status";
        }
    }

    public void approve() {
        if (applicationStatus.equalsIgnoreCase("approved")) {
            System.out.println("Application is already approved.");
        } else if (applicationStatus.equalsIgnoreCase("rejected")) {
            System.out.println("Cannot approve. Application is already rejected.");
        } else {
            applicationStatus = "approved";
            System.out.println("Application approved successfully.");
        }
    }

    public boolean reject() {
        if (applicationStatus.equalsIgnoreCase("rejected")) {
            return false;
        }
        if (applicationStatus.equalsIgnoreCase("approved")) {
            return false;
        }

        applicationStatus = "rejected";
        return true;
    }

}

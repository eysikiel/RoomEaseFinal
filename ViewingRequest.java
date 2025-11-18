import java.util.Date;

public class ViewingRequest {
    private String requestID;
    private String applicantID;
    private String roomID;
    private Date scheduleDate;

    public ViewingRequest(String requestID, String applicantID, String roomID, Date scheduleDate) {
        this.requestID = requestID;
        this.applicantID = applicantID;
        this.roomID = roomID;
        this.scheduleDate = scheduleDate;
    }

    public Object getRequestID() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}

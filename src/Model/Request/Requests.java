package Model.Request;

import Enums.RequestStatus;
import Model.Property.Room;

public abstract class Requests {
    private String requestID;
    private Room roomID;;
    private RequestStatus requestStatus;

    public Requests(String requestID, Room roomID, RequestStatus requestStatus) {
        this.requestID = requestID;
        this.roomID = roomID;
        this.requestStatus = requestStatus;
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

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setRoomID(Room roomID) {
        this.roomID = roomID;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean updateStatus(String newStatus) {
        try {
            this.requestStatus = RequestStatus.valueOf(newStatus.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status: " + newStatus + ". Valid values are: "
                    + java.util.Arrays.toString(RequestStatus.values()));
            return false;
        }
    }

    public abstract void displayRequest();
}

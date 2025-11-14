
import java.util.LinkedList;
import java.util.List;

public class Room {

    private String roomID;
    private String roomNumber;
    private RoomType type;
    private double price;
    private int capacity;
    private List<String> amenities = new LinkedList<>();
    private RoomStatus status;
    private RoomPricingType pricingType;

    public Room(int capacity, double price, RoomPricingType pricingType,
            String roomID, String roomNumber, RoomStatus status, RoomType type) {

        this.capacity = capacity;
        this.price = price;
        this.pricingType = pricingType;
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.status = status;
        this.type = type;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setPricingType(RoomPricingType pricingType) {
        this.pricingType = pricingType;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public RoomPricingType getPricingType() {
        return pricingType;
    }

    public void displayInfo() {

    }

    public void updateInfo(Room updatedRoom) {
        if (updatedRoom == null) {
            System.out.println("No updated information provided.");
            return;
        }

        this.roomID = updatedRoom.getRoomID();
        this.roomNumber = updatedRoom.getRoomNumber();
        this.type = updatedRoom.getType();
        this.price = updatedRoom.getPrice();
        this.capacity = updatedRoom.getCapacity();
        this.amenities = new LinkedList<>(updatedRoom.getAmenities());
        this.status = updatedRoom.getStatus();
        this.pricingType = updatedRoom.getPricingType();
    }
}

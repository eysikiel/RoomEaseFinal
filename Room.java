
import java.util.Scanner;
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

    public Room(String roomID, String roomNumber, RoomType type, double price, int capacity, List<String> amenities,
            RoomStatus status, RoomPricingType pricingType) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
        this.amenities = amenities;
        this.status = status;
        this.pricingType = pricingType;
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

    public void setPricingType(PricingType pricingType) {
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
    }

}

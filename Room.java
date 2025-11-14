import java.util.LinkedList;

public class Room {
    private String roomID;
    private String roomNumber;
    private RoomType roomType;
    private double price;
    private int capacity;
    private LinkedList<String> amenities;
    private RoomStatus roomStatus;
    private RoomPricingType pricingType;

    public Room(String roomID, String roomNumber, RoomType roomType, double price, int capacity, LinkedList<String> amenities, RoomStatus roomStatus, RoomPricingType pricingType) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.amenities = amenities;
        this.roomStatus = roomStatus;
        this.pricingType = pricingType;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public LinkedList<String> getAmenities() {
        return amenities;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public RoomPricingType getPricingType() {
        return pricingType;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAmenities(LinkedList<String> amenities) {
        this.amenities = amenities;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public void setPricingType(RoomPricingType pricingType) {
        this.pricingType = pricingType;
    }

    public void displayInfo() {
        System.out.println("Room number: " + this.getRoomNumber());
        System.out.println("Room type: " + this.getRoomType());
        System.out.println("Price: P" + this.getPrice());
        System.out.println("Capacity: " + this.getCapacity());
        System.out.println("Amenities: " + this.getAmenities());
        System.out.println("Status: " + this.getRoomStatus());
        System.out.println("Pricing type: " + this.getPricingType());
    }

    public void updateInfo (Room updateRoom) {

    }
}

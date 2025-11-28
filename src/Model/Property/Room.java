package Model.Property;

import java.util.LinkedList;

import Enums.RoomPricingType;
import Enums.RoomStatus;
import Enums.RoomType;

public class Room {

    private String roomID;
    private String roomNumber;
    private RoomType type;
    private double price;
    private int capacity;
    private LinkedList<String> amenities = new LinkedList<>();
    private RoomStatus status;
    private RoomPricingType pricingType;

    public Room(int capacity, double price, RoomPricingType pricingType,
            String roomID, String roomNumber, RoomStatus status, RoomType type) {

        this.capacity = capacity;
        this.setPrice(price);
       // this.price = price;
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
        if (price > 0) {
            this.price = price;
        } else {
            System.out.println("Invalid price. Price cannot be zero or negative.");
        }
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAmenities(LinkedList<String> amenities) {
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

    public LinkedList<String> getAmenities() {
        return amenities;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public RoomPricingType getPricingType() {
        return pricingType;
    }

    public void displayInfo() {
        System.out.println("-------------------------------------------------");
        System.out.println("Room ID       : " + roomID);
        System.out.println("Room Number   : " + roomNumber);
        System.out.println("Room Type     : " + (type != null ? type : "N/A"));
        System.out.println("Price         : " + price);
        System.out.println("Capacity      : " + capacity);

        if (amenities.isEmpty()) {
            System.out.println("Amenities     : None");
        } else {
            System.out.print("Amenities     : ");
            for (int i = 0; i < amenities.size(); i++) {
                System.out.print(amenities.get(i));
                if (i < amenities.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        System.out.println("Status        : " + (status != null ? status : "Vacant"));
        System.out.println("Pricing Type  : " + (pricingType != null ? pricingType : "Per_Head"));
        System.out.println("-------------------------------------------------");
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

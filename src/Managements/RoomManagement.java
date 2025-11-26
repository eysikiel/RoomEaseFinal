package Managements;

import Enums.RoomPricingType;
import Enums.RoomStatus;
import Enums.RoomType;
import Model.Property.Room;
import Database.DatabaseManagement;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class RoomManagement {

    private Scanner input = new Scanner(System.in);
    private LinkedList<Room> roomList;
    
    private int choice;

    
    public RoomManagement() {
        loadRoomsFromDatabase();
    }

    
    private void loadRoomsFromDatabase() {
        this.roomList = DatabaseManagement.getRooms();
        if (this.roomList == null) {
            this.roomList = new LinkedList<>();
        }
    }

    
    private void saveRoomsToDatabase() {
        DatabaseManagement.saveRooms(roomList);
    }

    public void displayMenu() {
        do {
            System.out.println("-------------------------------------------------");
            System.out.println("              ROOM MANAGEMENT MENU               ");
            System.out.println("-------------------------------------------------");
            System.out.println("[1] View Rooms");
            System.out.println("[2] Add Room");
            System.out.println("[3] Edit Room");
            System.out.println("[4] Delete Room");
            System.out.println("[5] Exit");
            System.out.println("-------------------------------------------------");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();
                input.nextLine();

                if (choice < 1 || choice > 5) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    continue;
                }

                handleLandlordChoice(choice);

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number only.");
                input.nextLine();
            }
        } while (choice != 5);
    }

    public void handleLandlordChoice(int choice) {
        switch (choice) {
            case 1:
                viewRooms();
                break;
            case 2:
                addRoom();
                break;
            case 3:
                editRoom();
                break;
            case 4:
                deleteRoomMenu();
                break;
            case 5:
                System.out.println("Exiting Room Management...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void viewRooms() {
        
        loadRoomsFromDatabase();

        if (roomList.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.println("-------------------------------------------------");
            System.out.println("                  View Rooms                     ");
            System.out.println("-------------------------------------------------");
            for (Room room : roomList) {
                room.displayInfo();
            }
        }
    }

    public void addRoom() {
        System.out.println("-------------------------------------------------");
        System.out.println("                  Add New Room                   ");
        System.out.println("-------------------------------------------------");

        System.out.print("Enter room number: ");
        String roomNumber = input.nextLine();

        
        for (Room room : roomList) {
            if (room.getRoomNumber().equals(roomNumber)) {
                System.out.println("Room number already exists! Please use a different room number.");
                return;
            }
        }

        System.out.println("Select Room Type:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Shared");
        int typeChoice = input.nextInt();
        input.nextLine();

        RoomType roomType;
        switch (typeChoice) {
            case 1:
                roomType = RoomType.Single;
                break;
            case 2:
                roomType = RoomType.Double;
                break;
            case 3:
                roomType = RoomType.Shared;
                break;
            default:
                System.out.println("Invalid choice! Defaulting to Single.");
                roomType = RoomType.Single;
        }

        System.out.print("Enter room price: ");
        double price = input.nextDouble();
        input.nextLine();

        System.out.print("Enter capacity: ");
        int capacity = input.nextInt();
        input.nextLine();

        System.out.println("Select Pricing Type:");
        System.out.println("1. Per Head");
        System.out.println("2. Per Room");
        int pricingChoice = input.nextInt();
        input.nextLine();

        RoomPricingType pricingType;
        if (pricingChoice == 1) {
            pricingType = RoomPricingType.per_head;
        } else if (pricingChoice == 2) {
            pricingType = RoomPricingType.full;
        } else {
            System.out.println("Invalid choice! Defaulting to Per Head.");
            pricingType = RoomPricingType.per_head;
        }

        
        String roomID = "R" + (roomList.size() + 1);

        Room newRoom = new Room(capacity, price, pricingType, roomID, roomNumber, RoomStatus.Vacant, roomType);

        roomList.add(newRoom);
        saveRoomsToDatabase();
        System.out.println("Room added successfully!");
    }

    public void editRoom() {
        
        loadRoomsFromDatabase();

        if (roomList.isEmpty()) {
            System.out.println("No rooms available to edit.");
            return;
        }

        System.out.print("Enter Room Number to edit: ");
        String roomNumberToEdit = input.nextLine();

        Room roomToEdit = null;
        for (Room room : roomList) {
            if (room.getRoomNumber().equals(roomNumberToEdit)) {
                roomToEdit = room;
                break;
            }
        }

        if (roomToEdit == null) {
            System.out.println("Room not found!");
            return;
        }

        System.out.println("Select field to edit:");
        System.out.println("1. Room Number");
        System.out.println("2. Room Type");
        System.out.println("3. Price");
        System.out.println("4. Capacity");
        System.out.println("5. Status");
        System.out.println("6. Cancel");
        System.out.print("Enter choice (1-6): ");
        int editChoice = input.nextInt();
        input.nextLine();

        switch (editChoice) {
            case 1:
                System.out.print("Enter new Room Number: ");
                String newNumber = input.nextLine();

                
                for (Room room : roomList) {
                    if (room.getRoomNumber().equals(newNumber) && !room.getRoomID().equals(roomToEdit.getRoomID())) {
                        System.out.println("Room number already exists! Please use a different room number.");
                        return;
                    }
                }

                roomToEdit.setRoomNumber(newNumber);
                System.out.println("Room number updated successfully!");
                break;

            case 2:
                System.out.println("Select Room Type:");
                System.out.println("1. Single");
                System.out.println("2. Double");
                System.out.println("3. Shared");
                int typeChoice = input.nextInt();
                input.nextLine();

                switch (typeChoice) {
                    case 1:
                        roomToEdit.setType(RoomType.Single);
                        break;
                    case 2:
                        roomToEdit.setType(RoomType.Double);
                        break;
                    case 3:
                        roomToEdit.setType(RoomType.Shared);
                        break;
                    default:
                        System.out.println("Invalid type! No changes made.");
                        return;
                }
                System.out.println("Room type updated successfully!");
                break;

            case 3:
                System.out.print("Enter new Price: ");
                double newPrice = input.nextDouble();
                input.nextLine();
                roomToEdit.setPrice(newPrice);
                System.out.println("Price updated successfully!");
                break;

            case 4:
                System.out.print("Enter new Capacity: ");
                int newCapacity = input.nextInt();
                input.nextLine();
                roomToEdit.setCapacity(newCapacity);
                System.out.println("Capacity updated successfully!");
                break;

            case 5:
                System.out.println("Select Status:");
                System.out.println("1. Vacant");
                System.out.println("2. Occupied");
                System.out.println("3. Under Maintenance");
                int statusChoice = input.nextInt();
                input.nextLine();

                switch (statusChoice) {
                    case 1:
                        roomToEdit.setStatus(RoomStatus.Vacant);
                        break;
                    case 2:
                        roomToEdit.setStatus(RoomStatus.Occupied);
                        break;
                    case 3:
                        roomToEdit.setStatus(RoomStatus.Under_Maintenance);
                        break;
                    default:
                        System.out.println("Invalid status! No changes made.");
                        return;
                }
                System.out.println("Status updated successfully!");
                break;

            case 6:
                System.out.println("Edit cancelled.");
                return;

            default:
                System.out.println("Invalid choice! No changes made.");
                break;
        }

                saveRoomsToDatabase();
                        saveRoomsToDatabase();
        System.out.println("Room updated successfully!");
    }

    public void deleteRoomMenu() {
        
        loadRoomsFromDatabase();

        if (roomList.isEmpty()) {
            System.out.println("No rooms available to delete.");
            return;
        }

        System.out.print("Enter Room ID to delete: ");
        String roomID = input.nextLine();

        for (Room room : roomList) {
            if (room.getRoomID().equals(roomID)) {
                roomList.remove(room);
                saveRoomsToDatabase();
                System.out.println("Room " + roomID + " successfully deleted!");
                return;
            }
        }

        System.out.println("Room with ID " + roomID + " not found.");
    }
}
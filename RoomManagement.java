
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;

public class RoomManagement {

    private Scanner input = new Scanner(System.in);

    private List<Room> roomList = new LinkedList<>();
    int choice;

    public RoomManagement(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public void displayMenu() {
        do {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("              ROOM MANAGEMENT MENU             ");
            System.out.println("───────────────────────────────────────────────");
            System.out.println("[1] View Rooms");
            System.out.println("[2] Add Room");
            System.out.println("[3] Update Room Information");
            System.out.println("[4] Remove Room");
            System.out.println("[5] Back to Landlord Main Menu");
            System.out.println("───────────────────────────────────────────────");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();

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
                System.out.println("Opening Applications and Reservations...");
            case 4:
                System.out.println("Opening Viewing Requests...");
            case 5:
                System.out.println("Opening Maintenance Requests...");
            case 6:
                System.out.println("Opening Contract Management...");
            case 7:
                System.out.println("Opening Billing and Payments...");
            case 8:
                System.out.println("Opening Reports and Analytics...");
            case 9:
                System.out.println("Opening System Settings...");
            case 10:
                System.out.println("Logging out... Goodbye!");
            default:
                System.out.println("Invalid choice. Please try again.");

        }

    }

    public void viewRooms() {

        if (roomList.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("                  View Rooms                   ");
            System.out.println("───────────────────────────────────────────────");
            for (Room room : roomList) {
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Type       : " + (room.getType() != null ? room.getType() : "N/A"));
                System.out.println("Price      : " + room.getPrice());
                System.out.println("Capacity   : " + room.getCapacity());
                System.out.println("Status     : " + (room.getStatus() != null ? room.getStatus() : "VACANT"));
                System.out.println("───────────────────────────────────────────────");
            }
        }
    }

    public String addRoom() {
        System.out.println("───────────────────────────────────────────────");
        System.out.println("                  Add New Room                 ");
        System.out.println("───────────────────────────────────────────────");
        System.out.print("Enter room number: ");
        String roomNumber = input.nextLine();

        System.out.println("Select Room Type:");
        System.out.println("1. Single");
        System.out.println("2. Double");
        System.out.println("3. Shared");
        System.out.print("Enter choice (1-3): ");
        int choice = input.nextInt();

        RoomType roomType;

        switch (choice) {
            case 1:
                roomType = RoomType.Single;
                break;
            case 2:
                roomType = RoomType.Double;
                break;
            case 3:
                roomType = RoomType.Single;
                break;
            default:
                System.out.println("Invalid choice! Defaulting to SINGLE.");
                roomType = RoomType.Single;
        }

        System.out.print("Enter room price: ");
        double price = input.nextDouble();

        System.out.print("Enter capacity: ");
        int capacity = input.nextInt();

        return "Room added successfully!";

    }

    public void editRoom() {

        if (roomList.isEmpty()) {
            System.out.println("No rooms available to edit.");
            return;
        }

        System.out.println("───────────────────────────────────────────────");
        System.out.println("                  Edit Room                    ");
        System.out.println("───────────────────────────────────────────────");

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
                System.out.println("1. VACANT");
                System.out.println("2. OCCUPIED");
                System.out.println("3. MAINTENANCE");

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

        System.out.println("Room updated successfully!");
    }

    public void deleteRoom(String roomID) {
        if (roomList.isEmpty()) {
            System.out.println("No rooms available to delete.");
            return;
        }

        for (Room room : roomList) {
            if (room.getRoomID().equals(roomID)) {
                roomList.remove(room);
                System.out.println("Room " + roomID + " successfully deleted!");
                return;
            }
        }

        System.out.println("Room with ID " + roomID + " not found.");
    }

}

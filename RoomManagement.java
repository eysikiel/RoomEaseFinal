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
                
            // roomManagement.displaySubmenu();
            case 2:
                System.out.println(addRoom());
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

    }
    
    public void addRoom() {
        int roomNumber;
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
                roomType = RoomType.
                break;
            case 2: 
                roomType = RoomType.DOUBLE; 
                break;
            case 3: 
                roomType = RoomType.SHARED; 
                break;
            default:
                System.out.println("Invalid choice! Defaulting to SINGLE.");
                roomType = RoomType.SINGLE;
        }

        System.out.print("Enter room price: ");
        double price = input.nextDouble();

        System.out.print("Enter capacity: ");
        int capacity = input.nextInt();


            
    }
    
    public void editRoom(String roomID, Room updatedRoom) {

    }
    public void deleteRoom(String roomID) {}

    
}
    


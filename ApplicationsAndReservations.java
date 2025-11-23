import java.util.InputMismatchException;
import java.util.LinkedList;

public class ApplicationsAndReservations {
    LinkedList<Application> applicationList;
    int choice;

    public ApplicationsAndReservations(LinkedList<Application> applicationList) {
        this.applicationList = applicationList;
    }

    



        public void displayMenu() {
        do {
            System.out.println("───────────────────────────────────────────────");
            System.out.println("      APPLICATIONS AND RESERVATIONS MENU       ");
            System.out.println("───────────────────────────────────────────────");
            System.out.println("[1] View Pending Applications");
            System.out.println("[2] Update Applications");
            System.out.println("[3] Confirm Reservation Payment (Convert to Tenant)");
            System.out.println("[4] Exit");
            System.out.println("───────────────────────────────────────────────");
            System.out.print("Enter your choice: ");

            try {
                choice = input.nextInt();
                input.nextLine();

                if (choice < 1 || choice > 4) {
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
                
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 4:
                
                break;
            case 5:
                System.out.println("Exiting Applications and Reservations...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    

    
}

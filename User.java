import java.util.Scanner;

public abstract class User {

    protected String userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    protected Role role;

    public User(String contactNumber, String firstName, String lastName, String password, String userID,
            String username, Role role) {
        this.contactNumber = contactNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userID = userID;
        this.username = username;
        this.role = role;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        ADMIN,
        LANDLORD,
        TENANT
    }

    public void displayLogInMenu() {
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        while (true) {
            System.out.println("================== WELCOME to ROOMEASE! ==================");
            System.out.println("[1] Log In");
            System.out.println("[2] Log Out");
            System.out.println("[3] Exit");
            System.out.print("Please select an option (Pick from 1-3): ");

            switch (choice) {
                case 1:
                    System.out.print("Enter Username: ");
                    String username = input.nextLine();
                    if (username.isEmpty()) {
                        System.out.println("Username cannot be empty. Please try again.");
                        return;
                    }
                    System.out.print("Enter Password: ");
                    String password = input.nextLine();

                    if (verifyLogin(username, password)) {
                        System.out.println("Login successful! Welcome, " + this.getFullName() + "!");
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;

                case 2:
                    if (this.username == null) {
                        System.out.println("No user is currently logged in.");
                    } else {
                        logout();
                    }
                    break;

                default:
                    System.out.print("Invalid input. Please enter a number between 1 and 3.");
                    break;
            }
        }
    }

    public boolean verifyLogin(String username, String password) {
        System.out.println("Verifying credentials for user: " + username + "...");
        System.out.println("Welcome, " + this.firstName + " " + this.lastName + "! " + " (" + username + ") ");
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("User " + this.username + " has logged out.");
    }

    // public abstract void displayProfile() {}

}

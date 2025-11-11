import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public abstract class User {

    protected String userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    private static List<User> users = new ArrayList<>();
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
public boolean verifyLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

public void logout(User currentUser) {
        System.out.println("User " + currentUser.getFullName() + " has logged out.");
    }

public void displayProfile(User currentUser) {
    System.out.println("User Profile:");
    System.out.println("User ID: " + currentUser.getUserID());
    System.out.println("Username: " + currentUser.getUsername());
    System.out.println("Name: " + currentUser.getFullName());
    System.out.println("Contact Number: " + currentUser.getContactNumber());
    System.out.println("Role: " + currentUser.getRole());
}

public void displayLogInMenu() {
    Scanner input = new Scanner(System.in);
    User currentUser = null;

        while (true) {
            System.out.println("\n================== WELCOME to ROOMEASE! ==================");
            System.out.println("[1] Register New Account");
            System.out.println("[2] Log In");
            System.out.println("[3] Log Out");
            System.out.println("[4] Exit");
            System.out.println("[5] Display Profile");
            System.out.print("Please select an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-5.");
                continue;
            }

            switch (choice) {
                case 1: // register
                    if (currentUser != null) {
                        System.out.println("You are already logged in as " + currentUser.getFullName() + ".");
                        break;
                    }

                    try {
                        System.out.print("Enter Username: ");
                        String newUsername = input.nextLine().trim();
                        if (newUsername.isEmpty()) {
                            System.out.println("Username cannot be empty.");
                            break;
                        }

                        // dupes checker
                        boolean duplicate = false;
                        for (User u : users) {
                            if (u.getUsername().equalsIgnoreCase(newUsername)) {
                                duplicate = true;
                                break;
                            }
                        }

                        if (duplicate) {
                            System.out.println("This account already exists! Please use a different username.");
                            break;
                        }

                        System.out.print("Enter Password: ");
                        String newPassword = input.nextLine().trim();
                        System.out.print("Enter First Name: ");
                        String fName = input.nextLine().trim();
                        System.out.print("Enter Last Name: ");
                        String lName = input.nextLine().trim();
                        System.out.print("Enter Contact Number: ");
                        String contact = input.nextLine().trim();

                        // new user
                        User newUser = new User(contact, fName, lName, newPassword, UUID.randomUUID().toString(),
                                newUsername, Role.TENANT) {};

                        users.add(newUser);
                        System.out.println("Account created successfully for " + newUser.getFullName() + "!");

                    } catch (Exception e) {
                        System.out.println("Error creating account: " + e.getMessage());
                    }
                    break;

                case 2: // log in
                    if (currentUser != null) {
                        System.out.println("Already logged in as " + currentUser.getFullName() + ".");
                        break;
                    }

                    System.out.print("Enter Username: ");
                    String usernameInput = input.nextLine().trim();
                    System.out.print("Enter Password: ");
                    String passwordInput = input.nextLine().trim();

                    boolean found = false;
                    for (User u : users) {
                        if (u.verifyLogin(usernameInput, passwordInput)) {
                            currentUser = u;
                            System.out.println("Login successful! Welcome, " + u.getFullName() + "!");
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Invalid username or password.");
                    }
                    break;

                case 3: // log out
                    if (currentUser == null) {
                        System.out.println("No user is currently logged in.");
                    } else {
                        logout(currentUser);
                        currentUser = null;
                    }
                    break;

                case 4: // exit
                    System.out.println("Exiting the application... Goodbye!");
                    input.close();
                    return;

                case 5: // display profile
                    if (currentUser == null) {
                        System.out.println("No user is currently logged in.");
                    } else {
                        displayProfile(currentUser);
                    }
                    break;

                default:
                    System.out.println("Invalid input! Please enter a number between 1-5.");
                    break;
            }
        }
    }
}
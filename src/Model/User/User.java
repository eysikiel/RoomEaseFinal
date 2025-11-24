package Model.User;

import Database.DatabaseManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// import java.util.function.Function;

public abstract class User {

    protected String userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    protected Role role;
    private static final List<User> users = new ArrayList<>();

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

    public static List<User> getUsers() {
        return users;
    }

    /**
     * Generate the next sequential user ID (USR1, USR2, etc.)
     * Fills gaps if users are deleted.
     */
    public static String generateNextUserID() {
        int nextID = 1;
        boolean found;

        while (true) {
            String candidateID = "USR" + nextID;
            found = false;

            // Check if this ID already exists
            for (User u : users) {
                if (u.getUserID().equals(candidateID)) {
                    found = true;
                    break;
                }
            }

            // If ID doesn't exist, use it
            if (!found) {
                return candidateID;
            }

            nextID++;
        }
    }

    public enum Role {
        LANDLORD,
        TENANT,
        APPLICANT
    }

    public boolean verifyLogin(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout(User currentUser) {
        System.out.println("User " + currentUser.getFullName() + " has logged out.");
    }

    public void displayProfile(User currentUser) {
        System.out.println("\n================== User Profile ==================\n");
        System.out.println("User ID: " + currentUser.getUserID());
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Name: " + currentUser.getFullName());
        System.out.println("Contact Number: " + currentUser.getContactNumber());
        System.out.println("Role: " + currentUser.getRole());
        System.out.println("\n=================================================\n");
    }

    public abstract void displayRoleMenu();

    // private static String errorCatching(Scanner input, String prompt,
    // Function<String, String>) { --> just in case the coding is too redundabt cuz
    // of the errors lawwwwl
    // while (true) {
    // System.out.print(prompt);
    // String userInput = input.nextLine().trim();
    // try {
    // String error = validator.apply(userInput);
    // if (error != null) {
    // System.out.println(error + "\n");
    // continue;
    // }
    // return userInput;
    // } catch (Exception e) {
    // System.out.println("Error: " + e.getMessage() + "\n");
    // }
    // }
    // }
    public void displayLogInMenu() { // i think a back button is needed
        Scanner input = new Scanner(System.in);
        User currentUser = null;

        while (true) {

            // If user is logged in, show role menu instead
            if (currentUser != null) {
                currentUser.displayRoleMenu();
                continue; // Back to main menu after role menu exits
            }

            System.out.println("\n\n================== WELCOME to ROOMEASE! ==================\n");
            System.out.println("[1] Register New Account");
            System.out.println("[2] Log In");
            System.out.println("[3] Log Out");
            System.out.println("[4] Exit");
            System.out.println("[5] Display Profile");
            System.out.println("\n==========================================================\n");
            System.out.print("Please select an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1-5.");
                System.out.println("\n==========================================================\n");
                continue;
            }

            switch (choice) {
                case 1: // register
                    System.out.println("\n\n======================== Register ===========================");

                    try {
                        String newUsername;
                        while (true) {
                            System.out.print("\nEnter Username: ");
                            newUsername = input.nextLine().trim();
                            if (newUsername.isEmpty()) {
                                System.out.println("Username cannot be empty. Try again.\n");
                                continue;
                            }

                            boolean duplicate = false;
                            for (User u : users) {
                                if (u.getUsername().equals(newUsername)) {
                                    duplicate = true;
                                    break;
                                }
                            }

                            if (duplicate) {
                                System.out.println("This username already exists! Please use a different username.\n");
                            } else {
                                break;
                            }
                        }

                        String newPassword;
                        while (true) {
                            System.out.print("Enter Password: ");
                            newPassword = input.nextLine().trim();
                            if (newPassword.isEmpty()) {
                                System.out.println("Password cannot be empty. Try again.\n");
                            } else {
                                break;
                            }
                        }

                        String fName;
                        while (true) {
                            System.out.print("Enter First Name: ");
                            fName = input.nextLine().trim();
                            if (fName.isEmpty()) {
                                System.out.println("First Name cannot be empty. Try again.\n");
                                continue;
                            } else if (!fName.matches("[A-Za-z]+")) {
                                System.out.println("First Name must only contain letters. Try again.\n");
                                continue;
                            }
                            fName = fName.substring(0, 1).toUpperCase() + fName.substring(1).toLowerCase();
                            break;
                        }

                        String lName;
                        while (true) {
                            System.out.print("Enter Last Name: ");
                            lName = input.nextLine().trim();
                            if (lName.isEmpty()) {
                                System.out.println("Last Name cannot be empty. Try again.\n");
                                continue;
                            } else if (!lName.matches("[A-Za-z]+")) {
                                System.out.println("Last Name must only contain letters. Try again.\n");
                                continue;
                            }
                            lName = lName.substring(0, 1).toUpperCase() + lName.substring(1).toLowerCase();
                            break;
                        }

                        String contact;
                        while (true) {
                            System.out.print("Enter Contact Number (+63 9XX-XXX-XXXX): ");
                            contact = input.nextLine().trim();
                            if (contact.isEmpty()) {
                                System.out.println("Contact Number cannot be empty. Try again.\n");
                                continue;
                            } else if (!contact.matches("\\+63 9\\d{2}-\\d{3}-\\d{4}")) {
                                System.out.println("Invalid format. Please use +63 9XX-XXX-XXXX.\n");
                                continue;
                            }
                            break;
                        }

                        // new user creation with sequential ID
                        String newUserID = generateNextUserID();
                        User newUser = new Applicant(contact, fName, lName, newPassword, newUserID,
                                newUsername, Role.APPLICANT);

                        DatabaseManagement.addUser(newUser);
                        System.out.println("\nAccount created successfully for " + newUser.getFullName() + "!\n");
                        System.out.println("=============================================================\n\n");

                    } catch (Exception e) {
                        System.out.println("\nError creating account: " + e.getMessage());
                        System.out.println("============================================================\n\n");
                    }
                    break;
                case 2: // log in
                    System.out.println("\n\n======================== Log In ============================");

                    boolean loggedIn = false; // bool to check login status
                    int loginAttempts = 0;
                    while (!loggedIn && loginAttempts < 3) {
                        try {
                            System.out.print("\nEnter Username (or type 'exit' to cancel): ");
                            String usernameInput = input.nextLine().trim();

                            if (usernameInput.equalsIgnoreCase("exit")) {
                                System.out.println("\nReturning to main menu...\n");
                                System.out.println("============================================================\n\n");
                                break;
                            }

                            if (usernameInput.isEmpty()) {
                                throw new IllegalArgumentException("Username cannot be empty.");
                            }

                            System.out.print("Enter Password: ");
                            String passwordInput = input.nextLine().trim();

                            if (passwordInput.isEmpty()) {
                                throw new IllegalArgumentException("Password cannot be empty.");
                            }

                            boolean found = false;
                            for (User u : users) {
                                if (u.verifyLogin(usernameInput, passwordInput)) {
                                    currentUser = u;
                                    System.out.println("\nLogin successful! Welcome, " + u.getFullName() + "!\n");
                                    System.out.println(
                                            "============================================================\n\n");
                                    found = true;
                                    loggedIn = true;
                                    break;
                                }
                            }

                            if (!found) {
                                loginAttempts++;
                                System.out.println("\nInvalid username or password. Please try again.\n");
                                System.out.println("Attempts remaining: " + (3 - loginAttempts) + "\n");
                                System.out.println("============================================================\n\n");
                            }

                        } catch (IllegalArgumentException e) {
                            System.out.println("\nError: " + e.getMessage());
                            System.out.println("Please try again.\n");
                            System.out.println("============================================================\n\n");
                        } catch (Exception e) {
                            System.out.println("\nUnexpected error during login: " + e.getMessage());
                            System.out.println("Please try again.\n");
                            System.out.println("============================================================\n\n");
                        }
                    }

                    if (!loggedIn && loginAttempts >= 3) {
                        System.out.println("\nToo many failed login attempts. Returning to main menu.\n");
                        System.out.println("============================================================\n\n");
                    }
                    break;
                case 3: // log out (now handled after role menu)
                case 5: // display profile (handled in role menu)
                    System.out.println(
                            "This option is only available in your role menu. Please return from your role menu first.");
                    break;
                case 4: // exit
                    System.out.println("\nExiting the application... Goodbye!");
                    input.close();
                    System.out.println("\n============================================================");
                    return;
                default:
                    System.out.println("Invalid input! Please enter a number between 1-5.");
                    System.out.println("\n============================================================\n\n");
            }
        }
    }
}

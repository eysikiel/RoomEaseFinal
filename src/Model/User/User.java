package Model.User;

import Database.DatabaseManagement;
import Utilities.InputValidator;

import java.util.LinkedList;

public abstract class User {

    protected String userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    protected Role role;
    private static final LinkedList<User> users = new LinkedList<>();

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

    // Getter and setter methods
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

    public static LinkedList<User> getUsers() {
        return users;
    }

    // UPDATED ID GENERATION METHOD
    public static String generateNextUserID(Role role) {
        String prefix;
        switch (role) {
            case LANDLORD:
                prefix = "LND";
                break;
            case TENANT:
                prefix = "TNT";
                break;
            case APPLICANT:
                prefix = "APT";
                break;
            default:
                prefix = "USR";
        }
        
        int nextID = 1;
        boolean found;

        while (true) {
            String candidateID = prefix + String.format("%03d", nextID);
            found = false;

            // Check if this ID already exists
            for (User u : users) {
                if (u.getUserID().equals(candidateID)) {
                    found = true;
                    break;
                }
            }
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

    public void displayProfile() {
        System.out.println("-------------------------------------------------");
        System.out.println("                  USER PROFILE                   ");
        System.out.println("-------------------------------------------------");
        System.out.println("User ID: " + getUserID());
        System.out.println("Username: " + getUsername());
        System.out.println("Name: " + getFullName());
        System.out.println("Contact Number: " + getContactNumber());
        System.out.println("Role: " + getRole());
        System.out.println("-------------------------------------------------");
    }

    public abstract void displayRoleMenu();

    public void displayLogInMenu() {
        User currentUser = null;

        while (true) {
            // If user is logged in, show role menu instead
            if (currentUser != null) {
                currentUser.displayRoleMenu();
                currentUser = null; // Reset after returning from role menu
                continue;
            }

            // DISPLAY THE MENU FIRST
            System.out.println("----------------------------------------------------------");
            System.out.println("                   WELCOME to ROOMEASE!                   ");
            System.out.println("----------------------------------------------------------");
            System.out.println("[1] Register New Account");
            System.out.println("[2] Log In");
            System.out.println("[3] Exit");
            System.out.println("----------------------------------------------------------");
            
            // THEN GET THE CHOICE (NO MENU TITLE PARAMETER)
            int choice = InputValidator.getMenuChoice(3);
            if (choice == -1) {
                continue; // User cancelled, show menu again
            }

            switch (choice) {
                case 1:
                    registerNewAccount();
                    break;

                case 2:
                    currentUser = loginUser();
                    break;

                case 3:
                    System.out.println("\nExiting the application... Goodbye!");
                    System.out.println("----------------------------------------------------------");
                    return;
            }
        }
    }

    private void registerNewAccount() {
        System.out.println("----------------------------------------------------------");
        System.out.println("                     Register                             ");
        System.out.println("----------------------------------------------------------");

        try {
            // Get username with uniqueness check
            String newUsername = getUniqueUsername();
            if (newUsername == null) return; // User cancelled

            // Get password
            String newPassword = InputValidator.getNonEmptyString("Enter Password");
            if (newPassword == null) return; // User cancelled

            // Get first name
            String firstName = InputValidator.getValidName("Enter First Name");
            if (firstName == null) return; // User cancelled

            // Get last name
            String lastName = InputValidator.getValidName("Enter Last Name");
            if (lastName == null) return; // User cancelled

            // Get contact number
            String contact = InputValidator.getValidPHContactNumber("Enter Contact Number");
            if (contact == null) return; // User cancelled

            // UPDATED: Generate ID with APPLICANT role
            String newUserID = generateNextUserID(Role.APPLICANT);
            User newUser = new Applicant(contact, firstName, lastName, newPassword, newUserID,
                    newUsername, Role.APPLICANT);

            DatabaseManagement.addUser(newUser);
            users.add(newUser); // Add to linked list
            System.out.println("\nAccount created successfully for " + newUser.getFullName() + "!\n");
            System.out.println("Your Applicant ID: " + newUserID);
            System.out.println("----------------------------------------------------------");

        } catch (Exception e) {
            System.out.println("\nError creating account: " + e.getMessage());
            System.out.println("----------------------------------------------------------");
        }
    }

    private String getUniqueUsername() {
        String username;
        while (true) {
            username = InputValidator.getNonEmptyString("Enter Username");
            if (username == null) return null; // User cancelled

            // Check for duplicate username
            boolean duplicate = false;
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {
                System.out.println("This username already exists! Please use a different username.\n");
            } else {
                return username;
            }
        }
    }

    private User loginUser() {
        System.out.println("----------------------------------------------------------");
        System.out.println("                     Log In                               ");
        System.out.println("----------------------------------------------------------");

        int loginAttempts = 0;
        while (loginAttempts < 3) {
            // Get username
            String usernameInput = InputValidator.getNonEmptyString("Enter Username");
            if (usernameInput == null) {
                System.out.println("\nReturning to main menu...\n");
                System.out.println("----------------------------------------------------------");
                return null; // User cancelled
            }

            // Get password
            String passwordInput = InputValidator.getNonEmptyString("Enter Password");
            if (passwordInput == null) {
                System.out.println("\nReturning to main menu...\n");
                System.out.println("----------------------------------------------------------");
                return null; // User cancelled
            }

            // Verify login credentials
            for (User u : users) {
                if (u.verifyLogin(usernameInput, passwordInput)) {
                    System.out.println("\nLogin successful! Welcome, " + u.getFullName() + "!\n");
                    System.out.println("----------------------------------------------------------");
                    return u;
                }
            }

            loginAttempts++;
            System.out.println("\nInvalid username or password. Please try again.\n");
            System.out.println("Attempts remaining: " + (3 - loginAttempts) + "\n");
            System.out.println("----------------------------------------------------------");
        }

        System.out.println("\nToo many failed login attempts. Returning to main menu.\n");
        System.out.println("----------------------------------------------------------");
        return null;
    }
}
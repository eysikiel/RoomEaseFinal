import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Function;

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

    public static void addUser(User user) {
        users.add(user);
    }

    public Role getRole() {
        return role;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
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
        System.out.println("\n\n================== User Profile ==================\n");
        System.out.println("User ID: " + currentUser.getUserID());
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Name: " + currentUser.getFullName());
        System.out.println("Contact Number: " + currentUser.getContactNumber());
        System.out.println("Role: " + currentUser.getRole());
        System.out.println("\n=================================================\n");
    }

    public abstract void displayRoleMenu();

    private static String errorCatching(Scanner input, String prompt, Function<String, String> validator) {
        while (true) {
            System.out.print(prompt);
            String userInput = input.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit")) {
                return null;
            }

            try {
                String error = validator.apply(userInput);
                if (error != null) {
                    System.out.println(error + "\n");
                    continue;
                }
                return userInput;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private String getValidatedName(Scanner input, String prompt, String fieldName) {
        String name = errorCatching(input, prompt, (value) -> {
            if (value.isEmpty()) return fieldName + " cannot be empty.";
            if (!value.matches("[A-Za-z]+"))
                return fieldName + " must only contain letters.";
            return null;
        });
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    private int getMenuChoice(Scanner input, String prompt, int min, int max) {
        String choiceStr = errorCatching(input, prompt, (value) -> {
            if (value.equalsIgnoreCase("exit") || value.equalsIgnoreCase("back") || value.equalsIgnoreCase("cancel")) {
                return null;
            }
            
            try {
                int choice = Integer.parseInt(value);
                if (choice < min || choice > max) {
                    return "Please enter a number between " + min + " and " + max + ".";
                }
                return null;
            } catch (NumberFormatException e) {
                return "Invalid input! Please enter a number.";
            }
        });
        
        if (choiceStr == null) {
            return -1; 
        }
        
        return Integer.parseInt(choiceStr);
    }

    public void displayLogInMenu() { 
        Scanner input = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            System.out.println("\n\n================== WELCOME to ROOMEASE! ==================\n");
            System.out.println("[1] Register New Account");
            System.out.println("[2] Log In");
            System.out.println("[3] Log Out");
            System.out.println("[4] Exit");
            System.out.println("[5] Display Profile");
            System.out.println("\n==========================================================\n");

            System.out.println("Type 'exit' to cancel an action.");
            int choice = getMenuChoice(input, "Please select and option (1-5)", 1, 5);

            if (choice == -1) {
                System.out.println("Operation cancelled.");
                continue; 
            }


            switch (choice) {
                case 1: // register
                    System.out.println("\n\n======================== Register ===========================");
                    System.out.println("Type 'exit' at any prompt to cancel registration.");
                    if (currentUser != null) {
                        System.out.println("You are already logged in as " + currentUser.getFullName() + ".\n");
                        break;
                    }

                    try {
                        String newUsername = errorCatching(input, "\nEnter Username: ", (value) -> {
                            if (value.isEmpty())
                                return "Username cannot be empty.";
                            for (User u : users) {
                                if (u.getUsername().equals(value))
                                    return "This username already exists!";
                            }
                            return null;
                        });

                        String newPassword = errorCatching(input, "Enter Password: ", (value) -> {
                            if (value.isEmpty())
                                return "Password cannot be empty.";
                            return null;
                        });


                        String fName = getValidatedName(input, "Enter First Name: ", "First Name");
                        String lName = getValidatedName(input, "Enter Last Name: ", "Last Name");
                        
                        String contact = errorCatching(input, "Enter Contact Number (+63 9XX-XXX-XXXX): ", (value) -> {
                            if (value.isEmpty()) return "Contact Number cannot be empty.";
                            if (!value.matches("\\+63 9\\d{2}-\\d{3}-\\d{4}")) 
                                return "Invalid format. Please use +63 9XX-XXX-XXXX.";
                            return null;
                        });
                        
                        // new user creation
                        User newUser = new Applicant(newUsername, newPassword, fName, lName, contact, UUID.randomUUID().toString(),
                                Role.APPLICANT);

                        users.add(newUser);
                        System.out.println("\nAccount created successfully for " + newUser.getFullName() + "!\n");
                        System.out.println("=============================================================\n\n");

                    } catch (Exception e) {
                        System.out.println("\nError creating account: " + e.getMessage());
                        System.out.println("============================================================\n\n");
                    }
                    break;

                case 2: // log in
                    if (currentUser != null) {
                        System.out.println("\nAlready logged in as " + currentUser.getFullName() + ".\n");
                        System.out.println("==========================================================\n\n");
                        break;
                    }

                    System.out.println("\n\n======================== Log In ============================");
                    System.out.println("Type 'exit' at any prompt to cancel Log In.");

                    String usernameInput = errorCatching(input, "\nEnter Username: ", (value) -> {
                        if (value.isEmpty()) return "Username cannot be empty.";
                        return null;
                    });
                    
                    if (usernameInput == null) { 
                        System.out.println("\nReturning to main menu...\n");
                        System.out.println("============================================================\n\n");
                        break;
                    }

                    String passwordInput = errorCatching(input, "Enter Password: ", (value) -> {
                        if (value.isEmpty()) return "Password cannot be empty.";
                        return null;
                    });

                    boolean found = false;
                    for (User u : users) {
                        if (u.verifyLogin(usernameInput, passwordInput)) {
                            currentUser = u;
                            System.out.println("\nLogin successful! Welcome, " + u.getFullName() + "!\n");
                            System.out.println("============================================================\n\n");
                            found = true;

                            currentUser.displayRoleMenu();
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("\nInvalid username or password. Please try again.\n");
                        System.out.println("============================================================\n\n");
                    }
                    break;

                case 3: // log out
                    if (currentUser == null) {
                        System.out.println("\nNo user is currently logged in.");
                        System.out.println("============================================================\n\n");
                    } else {
                        logout(currentUser);
                        currentUser = null;
                    }
                    break;

                case 4: // exit
                    System.out.println("\nExiting the application... Goodbye!");
                    input.close();
                    System.out.println("\n============================================================");
                    return;

                case 5: // display profile
                    if (currentUser == null) {
                        System.out.println("\nNo user is currently logged in.");
                        System.out.println("\n==========================================================\n\n");
                    } else {
                        displayProfile(currentUser);
                        System.out.println("============================================================\n\n");
                    }
                    break;

                default:
                    System.out.println("Invalid input! Please enter a number between 1-5.");
                    System.out.println("\n============================================================\n\n");
                    break;
            }
        }
    }
}
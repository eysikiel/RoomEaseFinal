package Utilities;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {
    private static Scanner input = new Scanner(System.in);

    // Validate integer input within range with cancel option
    public static int getValidInt(int min, int max, String prompt) {
        int userInput;
        do {
            System.out.print(prompt + " (Enter 0 to cancel): ");
            try {
                userInput = input.nextInt();
                input.nextLine(); // consume newline
                
                if (userInput == 0) {
                    return -1; // Cancel signal
                }
                
                if (userInput >= min && userInput <= max) {
                    return userInput;
                } else {
                    System.out.printf("Invalid input. Please enter a number between %d and %d, or 0 to cancel.\n", min, max);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number only, or 0 to cancel.");
                input.nextLine(); // clear invalid input
            }
        } while (true);
    }

    // Validate double input within range with cancel option
    public static double getValidDouble(double min, double max, String prompt) {
        double userInput;
        do {
            System.out.print(prompt + " (Enter 0 to cancel): ");
            try {
                userInput = input.nextDouble();
                input.nextLine(); // consume newline
                
                if (userInput == 0) {
                    return -1; // Cancel signal
                }
                
                if (userInput >= min && userInput <= max) {
                    return userInput;
                } else {
                    System.out.printf("Invalid input. Please enter a number between %.2f and %.2f, or 0 to cancel.\n", min, max);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number, or 0 to cancel.");
                input.nextLine(); // clear invalid input
            }
        } while (true);
    }

    // Validate non-empty string with cancel option
    public static String getNonEmptyString(String prompt) {
        String userInput;
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            userInput = input.nextLine().trim();
            
            if (userInput.equalsIgnoreCase("cancel")) {
                return null; // Cancel signal
            }
            
            if (!userInput.isEmpty()) {
                return userInput;
            } else {
                System.out.println("Input cannot be empty. Please try again or enter 'cancel' to go back.");
            }
        } while (true);
    }

    // Validate email format with cancel option
    public static String getValidEmail(String prompt) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        String email;
        
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            email = input.nextLine().trim();
            
            if (email.equalsIgnoreCase("cancel")) {
                return null; // Cancel signal
            }
            
            if (pattern.matcher(email).matches()) {
                return email;
            } else {
                System.out.println("Invalid email format. Please try again or enter 'cancel' to go back.");
            }
        } while (true);
    }

    // Validate phone number with cancel option
    public static String getValidPhoneNumber(String prompt) {
        String phone;
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            phone = input.nextLine().trim();
            
            if (phone.equalsIgnoreCase("cancel")) {
                return null; // Cancel signal
            }
            
            // Basic validation - at least 10 digits
            if (phone.matches("\\d{10,}")) {
                return phone;
            } else {
                System.out.println("Invalid phone number. Please enter at least 10 digits, or 'cancel' to go back.");
            }
        } while (true);
    }

    // Validate Philippine contact number format
    public static String getValidPHContactNumber(String prompt) {
        String contact;
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            contact = input.nextLine().trim();
            
            if (contact.equalsIgnoreCase("cancel")) {
                return null;
            }
            
            if (contact.matches("\\+63 9\\d{2}-\\d{3}-\\d{4}")) {
                return contact;
            } else {
                System.out.println("Invalid format. Please use +63 9XX-XXX-XXXX, or 'cancel' to go back.");
            }
        } while (true);
    }

    // Yes/No confirmation with cancel option
    public static Boolean getConfirmation(String prompt) {
        String userInput;
        do {
            System.out.print(prompt + " (y/n/cancel): ");
            userInput = input.nextLine().trim().toLowerCase();
            
            if (userInput.equals("cancel")) {
                return null; // Cancel signal
            }
            
            if (userInput.equals("y") || userInput.equals("yes")) {
                return true;
            } else if (userInput.equals("n") || userInput.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes, 'n' for no, or 'cancel' to go back.");
            }
        } while (true);
    }

    // Validate menu choice with cancel option (NO HEADER VERSION)
    public static int getMenuChoice(int maxOption) {
        int choice;
        do {
            System.out.print("Enter your choice (1-" + maxOption + ", or 0 to cancel): ");
            try {
                choice = input.nextInt();
                input.nextLine(); // consume newline
                
                if (choice == 0) {
                    return -1; // Cancel signal
                }
                
                if (choice >= 1 && choice <= maxOption) {
                    return choice;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + maxOption + ", or 0 to cancel.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number only, or 0 to cancel.");
                input.nextLine(); // clear invalid input
            }
        } while (true);
    }

    // Get string with minimum length with cancel option
    public static String getStringWithMinLength(String prompt, int minLength) {
        String userInput;
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            userInput = input.nextLine().trim();
            
            if (userInput.equalsIgnoreCase("cancel")) {
                return null; // Cancel signal
            }
            
            if (userInput.length() >= minLength) {
                return userInput;
            } else {
                System.out.println("Input must be at least " + minLength + " characters long. Please try again or enter 'cancel' to go back.");
            }
        } while (true);
    }

    // Get positive integer with cancel option
    public static int getPositiveInt(String prompt) {
        int userInput;
        do {
            System.out.print(prompt + " (Enter 0 to cancel): ");
            try {
                userInput = input.nextInt();
                input.nextLine(); // consume newline
                
                if (userInput == 0) {
                    return -1; // Cancel signal
                }
                
                if (userInput > 0) {
                    return userInput;
                } else {
                    System.out.println("Please enter a positive number, or 0 to cancel.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number only, or 0 to cancel.");
                input.nextLine(); // clear invalid input
            }
        } while (true);
    }

    // Get valid name (letters only) with cancel option
    public static String getValidName(String prompt) {
        String name;
        do {
            System.out.print(prompt + " (Enter 'cancel' to go back): ");
            name = input.nextLine().trim();
            
            if (name.equalsIgnoreCase("cancel")) {
                return null;
            }
            
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.\n");
            } else if (!name.matches("[A-Za-z ]+")) {
                System.out.println("Name must only contain letters and spaces. Try again.\n");
            } else {
                // Capitalize first letter of each word
                String[] words = name.split("\\s+");
                StringBuilder capitalized = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        capitalized.append(word.substring(0, 1).toUpperCase())
                                  .append(word.substring(1).toLowerCase())
                                  .append(" ");
                    }
                }
                return capitalized.toString().trim();
            }
        } while (true);
    }
}
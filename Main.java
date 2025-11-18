public class Main {

    public static void main(String[] args) {
        // User app = new User("", "", "", "", "", "", User.Role.TENANT) {};

        User testUser = new User("+63 912-345-6789", "Hannah", "Fernandez", "12345", "tenant123", "lennywenny", User.Role.TENANT) {};
        User.addUser(testUser);
        User app = testUser;
        app.displayLogInMenu(); 
    }
}
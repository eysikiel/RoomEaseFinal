import Model.User.User;
import Model.User.Landlord;
import Database.DatabaseManagement;

public class Main {
    public static void main(String[] args) {
        // initialize JSON-backed user database
        DatabaseManagement.init();

        if (User.getUsers().isEmpty()) {
            // Create landlord using 7-parameter constructor
            User landlord = new Landlord("09561620716", "Lyka", "Lamparero", "lykalamparero", "USR01", "lyka",
                    User.Role.LANDLORD);
            User.getUsers().add(landlord);
            DatabaseManagement.saveUsers();
        }

        // launch the login/register menu from any user (they all share the menu)
        if (!User.getUsers().isEmpty()) {
            User.getUsers().get(0).displayLogInMenu();
        }
    }
}
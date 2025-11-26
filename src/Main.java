import Model.User.User;
import Model.User.Landlord;
import Database.DatabaseManagement;

public class Main {
    public static void main(String[] args) {

        DatabaseManagement.init();

        if (User.getUsers().isEmpty()) {

            User landlord = new Landlord("09561620716", "Lyka", "Lamparero", "lykalamparero", "USR01", "lyka",
                    User.Role.LANDLORD);
            User.getUsers().add(landlord);
            DatabaseManagement.saveUsers();
        }

        if (!User.getUsers().isEmpty()) {
            User.getUsers().get(0).displayLogInMenu();
        }
    }
}
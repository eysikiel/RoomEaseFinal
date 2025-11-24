package src;
import src.Model.User.Landlord;
import src.Model.User.User;

public class Main {
    public static void main(String[] args) {
        User landlord = new Landlord("09561620716", "Lyka", "Lamparero", "lykalamparero", "USR01", "lyka", User.Role.LANDLORD);
        User.getUsers().add(landlord);

        landlord.displayLogInMenu();
    }
}
public class Main {
    public static void main(String[] args) {
        User landlord = new Landlord("09561620716", "Lyka", "Amaguin", "lykaamaguin", "USR01", "lyka", User.Role.LANDLORD);
        User.getUsers().add(landlord);

        landlord.displayLogInMenu();
    }
}
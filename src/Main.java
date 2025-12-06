import Model.User.User;
import Model.User.Landlord;
import Model.User.Tenant;
import Database.DatabaseManagement;

public class Main {
    public static void main(String[] args) {
        DatabaseManagement.init();

        if (User.getUsers().isEmpty()) {
            User landlord = new Landlord("09561620716", "Lyka", "Lamparero", "lykalamparero", "USR01", "lyka",
                    User.Role.LANDLORD);
            User.getUsers().add(landlord);

            User tenant = new Tenant("09123456789", "Juan", "Dela Cruz", "password123", 
                User.generateNextUserID(User.Role.TENANT), "juantent", User.Role.TENANT,
                Tenant.generateNextTenantID(), null, null, 0.0, "09187654321");
            User.getUsers().add(tenant);

            DatabaseManagement.saveUsers();
        }

        if (!User.getUsers().isEmpty()) {
            User.getUsers().get(0).displayLogInMenu();
        }
    }
}
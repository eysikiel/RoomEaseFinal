public class Landlord extends User {
    private String landlordID;

    public Landlord(String landlordID, String contactNumber, String firstName, String lastName, String password, String userID, String username, Role role) {
        super(contactNumber, firstName, lastName, password, userID, username, role);
        this.landlordID = landlordID;
    }



    
}

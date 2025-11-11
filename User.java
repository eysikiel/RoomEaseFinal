public abstract class User {

    protected String userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String contactNumber;
    protected Role role;

    public User(String contactNumber, String firstName, String lastName, String password, String userID, String username, Role role) {
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

    public String getContactNumber() {
        return contactNumber;
    }

    public Role getRole() {
        return role;
    }

    public boolean verifyLogin(String username, String password) {
        System.out.println("Welcome to RoomEase!");
        System.out.println("Please enter your login credentials.");
        System.out.println("Verifying credentials for user: " + username);
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("User " + this.username + " has logged out.");
    }

    // public abstract void displayProfile() {}

}

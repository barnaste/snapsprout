package use_case.delete_user;

/**
 * The Input Data for the Delete User Case.
 */
public class DeleteUserInputData {
    private final String username;
    private final String password;

    public DeleteUserInputData(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
package game_logic;

/**
 * Created by HP on 2017-06-07.
 */
public class UserDTO {
    private String login;
    private String password;
    private boolean isBlocked;
    private boolean isPresent;

    public UserDTO(String login, String password, boolean isBlocked, boolean isPresent) {
        this.login = login;
        this.password = password;
        this.isBlocked = isBlocked;
        this.isPresent = isPresent;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}

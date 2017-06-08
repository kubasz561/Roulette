package game_logic;

/**
 * Created by HP on 2017-06-07.
 */
public class UserDTO {
    private String login;
    private String password;
    private int accountBalance;
    private boolean isBlocked;

    public UserDTO(String login, String password, int accountBalance, boolean isBlocked) {
        this.login = login;
        this.password = password;
        this.isBlocked = isBlocked;
        this.accountBalance = accountBalance;
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


    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}

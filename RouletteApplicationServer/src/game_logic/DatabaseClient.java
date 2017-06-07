package game_logic;

import communication.Client;

import java.sql.Connection;

/**
 * Created by HP on 2017-06-07.
 */
public class DatabaseClient {
    private Connection dbConnection;

    public DatabaseClient (Connection dbConnection){
        this.dbConnection = dbConnection;
    }

    public UserDTO searchUser(String login){
        return new UserDTO("Jan", "ok", false, true);
    }
    public boolean createUser(String login, String password){
        return true;
    }
    public int getClientAccount(Client client){
      return 2000;
    }
    public boolean updateClientAccount(Client client, int value){
        return true;
    }

}

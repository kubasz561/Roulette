package game_logic;

import communication.Client;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

/**
 * Created by HP on 2017-06-07.
 */
public class DatabaseClient {
    private Connection dbConnection;

    private String searchUserQuery = "select * from users where login = {0}";
    private String getClientAccountQuery = "select account_ballance from users where login = {0}";
    private String createUserQuery = "insert into users values ({0}, {1}, 1000, false)";
    private String updateClientAccountQuery = "update users set account_ballance = account_ballance + {0} where login ={1}";

    public DatabaseClient (Connection dbConnection){
        this.dbConnection = dbConnection;
    }

    public UserDTO searchUser(String login){

        Statement stmt = null;
        ResultSet result;
        login = "'"+login+"'";
        String query = MessageFormat.format(searchUserQuery, login);
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(query);
            if(result.next()) {
                String loginDb = result.getString("login");
                String passDb = result.getString("pass");
                int accountBallanceDb = result.getInt("account_ballance");
                boolean isUserBlockedDb = result.getBoolean("is_user_blocked");

                System.out.println(loginDb + "\t" + passDb + "\t" + accountBallanceDb + "\t" + isUserBlockedDb );
                return new UserDTO(loginDb,passDb,accountBallanceDb,isUserBlockedDb);
            }

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }/*
        return result;
        ResultSet result = execute(searchUserQuery);
        try {
            if(result.next()) {
                String loginDb = result.getString("login");
                String passDb = result.getString("pass");
                double accountBallanceDb = result.getInt("account_ballance");
                boolean isUserBlockedDb = result.getBoolean("is_user_blocked");

                System.out.println(loginDb + "\t" + passDb + "\t" + accountBallanceDb + "\t" + isUserBlockedDb );
                return new UserDTO(loginDb,passDb,accountBallanceDb,isUserBlockedDb);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }*/

        return null;
    }

    public boolean createUser(String login, String password){

        Statement stmt = null;
        int queryResult;
        boolean result = false;
        login = "'"+login+"'";
        password = "'"+password+"'";
        String query = MessageFormat.format(createUserQuery, login, password);
        try {
            stmt = dbConnection.createStatement();
            queryResult = stmt.executeUpdate(query);
            if (queryResult > 0)
                result = true;

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    public int getClientAccount(Client client){
        Statement stmt = null;
        ResultSet result;
        int accountBallanceDb = 0;
        String login = "'"+client.getLogin()+"'";
        String query = MessageFormat.format(getClientAccountQuery, login);
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(query);
            if(result.next()) {
                accountBallanceDb = result.getInt("account_ballance");

            }

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        return accountBallanceDb;
    }

    public boolean updateClientAccount(Client client, int value){
        Statement stmt = null;
        int queryResult;
        boolean result = false;
        String login = "'"+client.getLogin()+"'";
        String query = MessageFormat.format(updateClientAccountQuery, value, login);
        try {
            stmt = dbConnection.createStatement();
            queryResult = stmt.executeUpdate(query);
            if (queryResult > 0)
                result = true;

        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }
    private ResultSet execute(String query){
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(query);


        } catch (SQLException e ) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();

            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }


}

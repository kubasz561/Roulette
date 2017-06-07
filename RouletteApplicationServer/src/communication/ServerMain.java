package communication;

import com.sun.security.ntlm.Server;
import game_logic.ServerOverseer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 */
public class ServerMain {
    private static ServerSocket listener;

    public static void main(String[] args) throws IOException
    {
        //TODO: Pobać port z bazy danych
        try
        {

            ServerOverseer serverOverseer = ServerOverseer.getInstance();
            Connection dbConnection = connectToDb();
            if(dbConnection==null)
            {
                System.out.println("Couldn't connect to database");
                return;
            }
            serverOverseer.dbConnection = dbConnection;
            listener = new ServerSocket(1234);

            while (serverOverseer.isRunning)
            {
                System.out.println("Server listening for clients");
                Socket newClientSocket = listener.accept();
                System.out.println("New client connected");
                serverOverseer.gameLogicMutex.acquireUninterruptibly();
                Client newClient2 = new Client(newClientSocket);
                /*//ServerListeningThread newClient = new ServerListeningThread(newClientSocket);
                if(newClient.authenticatedSuccessfully) {
                    newClient.start();
                    System.out.println("New client authenticated");
                }
                else
                    System.out.println("Client tried connecting but couldn't authenticate");*/
                serverOverseer.gameLogicMutex.release();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("blad klienta");

        } finally {
            listener.close();
            System.out.println("Server stopped");
        }

    }

    private static Connection connectToDb() {
        String url = "jdbc:postgresql://35.157.21.83:5432/roulette";
        Properties props = new Properties();
        props.setProperty("user","roulette");
        props.setProperty("password","Yu1eT1saaBooF4eGhix5uuPhee7ahth0seHohd1roi9oxephah");
        props.setProperty("ssl","false");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

}

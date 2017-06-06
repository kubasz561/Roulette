package communication;

import game_logic.ServerOverseer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 */
public class ServerMain {
    private static ServerSocket listener;

    public static void main(String[] args) throws IOException
    {
        //TODO: Pobać port z bazy danych
        try
        {
            listener = new ServerSocket(1234);
            ServerOverseer serverOverseer = ServerOverseer.getInstance();


            while (serverOverseer.isRunning)
            {
                System.out.println("Server listening for clients");
                Socket newClientSocket = listener.accept();
                System.out.println("New client connected");
                serverOverseer.gameLogicMutex.acquireUninterruptibly();
                Client newClient2 = new Client(newClientSocket);
                //ServerListeningThread newClient = new ServerListeningThread(newClientSocket);
                if(newClient.authenticatedSuccessfully) {
                    newClient.start();
                    System.out.println("New client authenticated");
                }
                else
                    System.out.println("Client tried connecting but couldn't authenticate");
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

}

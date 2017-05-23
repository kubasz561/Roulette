package communication;

import game_logic.ServerGameLogic;
import game_logic.ServerOverseer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


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
                System.out.println();
                Socket newClientSocket = listener.accept();
                serverOverseer.gameLogicMutex.acquireUninterruptibly();
                ServerCommunicationThread newClient = new ServerCommunicationThread(newClientSocket);
                if(newClient.authenticatedSuccessfully)
                    newClient.run();
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

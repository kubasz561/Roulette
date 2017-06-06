package communication;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import game_logic.ServerOverseer;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerListeningThread extends Thread{
    private Socket socket;
    private ObjectInputStream clientToServer;
    private Semaphore clientCommunicationSemaphore = new Semaphore(1);
    public ServerOverseer serverOverseer = ServerOverseer.getInstance();
    public boolean authenticatedSuccessfully = false;
    public Client thisThreadsClient;
    private CommunicationManagerServer communicationManagerServer;

    public ServerListeningThread(ObjectInputStream stream, CommunicationManagerServer communicationManagerServer)
    {
        clientToServer = stream;
        this.communicationManagerServer = communicationManagerServer;
    }


    @Override
    public void run()
    {
        try
        {
            while (serverOverseer.isRunning)
            {
                    JSONMessage msg = (JSONMessage)clientToServer.readObject();
                    this.clientCommunicationSemaphore.acquireUninterruptibly();
                    serverOverseer.gameLogicMutex.acquireUninterruptibly();
                    serverOverseer.serverGameLogic.handleMessage(msg, thisThreadsClient);
                    serverOverseer.gameLogicMutex.release();
                    this.clientCommunicationSemaphore.release();
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            communicationManagerServer.closeAll();
        }
    }

}

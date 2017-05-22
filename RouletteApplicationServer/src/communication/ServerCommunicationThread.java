package communication;

import com.kubasz561.roulette.common.JSONMessage;
import game_logic.ServerOverseer;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerCommunicationThread extends Thread{
    private Socket socket;
    private ObjectInputStream clientToServer;
    private ObjectOutputStream serverToClient;
    private Semaphore clientCommunicationSemaphore = new Semaphore(1);
    public ServerOverseer serverOverseer = ServerOverseer.getInstance();
    public boolean authenticatedSuccessfully = false;

    public ServerCommunicationThread(Socket clientSocket)
    {
        try
        {
            socket = clientSocket;
            clientToServer = new ObjectInputStream(clientSocket.getInputStream());
            serverToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            Client connectedClient = new Client((JSONMessage)clientToServer.readObject());
            connectedClient.thisClientComThread = this;
            authenticatedSuccessfully = connectedClient.authenticateClient();
            if(authenticatedSuccessfully)
            {
                serverOverseer.gameLogicMutex.acquireUninterruptibly();
                serverOverseer.addNewClient(connectedClient);
                //TODO: send user JSONMessage describing current game state
                //sendMessage(serverOverseer.gameStateController.currentStateMessage)
                serverOverseer.gameLogicMutex.release();
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            closeConnection();
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        try
        {
            while (serverOverseer.isRunning)
            {
                if (clientToServer.available() > 0)
                {
                    this.clientCommunicationSemaphore.acquireUninterruptibly();
                    serverOverseer.gameLogicMutex.acquireUninterruptibly();
                    JSONMessage msg = (JSONMessage)clientToServer.readObject();
                    serverOverseer.serverGameLogic.handleMessage(msg);
                    serverOverseer.gameLogicMutex.release();
                    this.clientCommunicationSemaphore.release();
                }
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        closeConnection();
    }

    public void sendMessage(JSONMessage msg) throws IOException
    {
        if(serverOverseer.gameLogicMutex.availablePermits() == 0)
        {
            this.clientCommunicationSemaphore.acquireUninterruptibly();
            serverToClient.writeObject(msg);
            this.clientCommunicationSemaphore.release();
        }
        else
            throw new IOException("Not able to send a message because of missing mutexLock");
    }


    private void closeConnection() {
        try
        {
            serverToClient.close();
            clientToServer.close();
            socket.close();
            System.out.println("Disconnecting client successful");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            System.out.println("Exception was thrown during closing connection with client");
        }
    }
}
package communication;

import com.kubasz561.roulette.common.JSONMessage;
import game_logic.ServerOverseer;
import java.io.*;
import java.net.Socket;

public class ServerCommunicationThread extends Thread{
    private Socket socket;
    private ObjectInputStream clientToServer;
    private ObjectOutputStream serverToClient;
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
            //TODO: catch the fact that client haven't authenticated before running Thread
            if(authenticatedSuccessfully)
                serverOverseer.addNewClient(connectedClient);
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
                    serverOverseer.gameLogicSemaphore.acquireUninterruptibly();
                    JSONMessage msg = (JSONMessage)clientToServer.readObject();
                    serverOverseer.serverGameLogic.handleMessage(msg);
                    serverOverseer.gameLogicSemaphore.acquireUninterruptibly();
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
        if(serverOverseer.gameLogicSemaphore.availablePermits() == 0)
            serverToClient.writeObject(msg);
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

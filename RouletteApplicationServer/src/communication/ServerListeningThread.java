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
    public Client thisComThreadClient;
    public ServerSendingThread serverToClientThread;

    public ServerListeningThread(Socket clientSocket)
    {
        try
        {
            socket = clientSocket;
            clientToServer = new ObjectInputStream(clientSocket.getInputStream());
            serverToClientThread = new ServerSendingThread(clientSocket, clientCommunicationSemaphore);

            Client connectedClient = new Client((JSONMessage)clientToServer.readObject(), this, serverToClientThread);
            serverToClientThread.setClient(connectedClient); //ugly
            thisComThreadClient = connectedClient;
            authenticatedSuccessfully = connectedClient.authenticatedSuccesfully;
            if(authenticatedSuccessfully)
            {
                serverOverseer.addNewClient(connectedClient);
                serverToClientThread.sendMessage(JSONMessageBuilder.create_message(MessageType.SIGN_UP_OK,null));
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
                    JSONMessage msg = (JSONMessage)clientToServer.readObject();
                    this.clientCommunicationSemaphore.acquireUninterruptibly();
                    serverOverseer.gameLogicMutex.acquireUninterruptibly();
                    serverOverseer.serverGameLogic.handleMessage(msg, thisComThreadClient);
                    serverOverseer.gameLogicMutex.release();
                    this.clientCommunicationSemaphore.release();
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try
        {
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

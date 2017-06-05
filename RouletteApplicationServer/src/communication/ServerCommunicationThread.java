package communication;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import game_logic.ServerOverseer;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ServerCommunicationThread extends Thread{
    private Socket socket;
    private ObjectInputStream clientToServer;
    private Semaphore clientCommunicationSemaphore = new Semaphore(1);
    public ServerOverseer serverOverseer = ServerOverseer.getInstance();
    public boolean authenticatedSuccessfully = false;
    public Client thisThreadsClient;

    public ServerCommunicationThread(Socket clientSocket)
    {
        try
        {
            socket = clientSocket;
            clientToServer = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream serverToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            ServerSenderThread serverSenderThread = new ServerSenderThread(serverToClient);
            serverSenderThread.start();
            Client connectedClient = new Client((JSONMessage)clientToServer.readObject(), this, serverSenderThread.outgoingQueue);
            thisThreadsClient = connectedClient;
            authenticatedSuccessfully = connectedClient.authenticatedSuccesfully;
            if(authenticatedSuccessfully)
            {
                serverOverseer.addNewClient(connectedClient);
                thisThreadsClient.sendMessage(JSONMessageBuilder.create_message(MessageType.SIGN_UP_OK,null));
                //sendMessage(serverOverseer.gameStateController.currentStateMessage) //TODO: send user JSONMessage describing game state
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            closeIncomingStream();
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
            closeIncomingStream();
        }
    }

    private void closeIncomingStream() {
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

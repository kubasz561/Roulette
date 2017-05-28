package communication;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import game_logic.ServerOverseer;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ServerSendingThread extends Thread{
    private Socket socket;
    private ObjectOutputStream serverToClient;
    private Semaphore clientCommunicationSemaphore;
    private Semaphore queueSemaphore;
    public ServerOverseer serverOverseer = ServerOverseer.getInstance();
    public Client thisComThreadClient;
    private LinkedList msgQueue;

    public ServerSendingThread(Socket clientSocket, Semaphore clientCommunicationSemaphore) {
        try {
            socket = clientSocket;
            serverToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            //thisComThreadClient = connectedClient;
            this.clientCommunicationSemaphore = clientCommunicationSemaphore;
            msgQueue = new LinkedList();
            queueSemaphore = new Semaphore(1);

        }  catch (IOException e)  {
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
                if( !msgQueue.isEmpty() ){
                    this.queueSemaphore.acquireUninterruptibly();

                    this.clientCommunicationSemaphore.acquireUninterruptibly();
                    // serverOverseer.gameLogicMutex.acquireUninterruptibly();
                    serverToClient.writeObject(msgQueue.poll());
                    this.queueSemaphore.release();
                    //serverOverseer.gameLogicMutex.release();
                    this.clientCommunicationSemaphore.release();
                }

            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }

    public void sendMessage(JSONMessage msg)  {
        if(true) //GameLogicMutex tu był.
        {
            this.queueSemaphore.acquireUninterruptibly();
            msgQueue.add(msg);
            this.queueSemaphore.release();
        }
    }

    //TODO rethink this
    public void setClient(Client connectedClient){
        this.thisComThreadClient = connectedClient;
    }


    private void closeConnection() {
        try
        {
            serverToClient.close();
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

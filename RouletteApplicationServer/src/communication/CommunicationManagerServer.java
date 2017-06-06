package communication;

import com.kubasz561.roulette.common.JSONMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by HP on 2017-06-06.
 */
public class CommunicationManagerServer {
    private Socket socket;
    private ObjectInputStream clientToServer;
    public ObjectOutputStream serverToClient;
    public ServerListeningThread listeningThread;
    public ServerSenderThread senderThread;

    public CommunicationManagerServer(Socket newClientSocket, Client client){
        socket = newClientSocket;
        try {
            serverToClient = new ObjectOutputStream(socket.getOutputStream());
            clientToServer = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
        listeningThread = new ServerListeningThread(clientToServer, this, client);
        senderThread = new ServerSenderThread(serverToClient, this);
    }

    public void start(){
        listeningThread.start();
        senderThread.start();

    }
    public void sendMessage(JSONMessage msg) {
        try
        {
            senderThread.outgoingQueue.put(msg);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.out.println("Couldn't put message into outgoing queue");
        }
    }

    public void closeAll(){
        try {
            serverToClient.close();
            clientToServer.close();
            socket.close();
            System.out.println("Succesfully closed all connections");
        }catch (IOException e){
            System.out.println("Closing connections unsuccesfull");
            e.printStackTrace();
        }

    }
}

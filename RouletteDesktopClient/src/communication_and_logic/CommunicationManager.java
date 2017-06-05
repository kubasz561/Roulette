package communication_and_logic;

import com.kubasz561.roulette.common.JSONMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by HP on 2017-06-05.
 */
public class CommunicationManager {
    private Socket clientSocket;
    private ObjectOutputStream clientToServer;
    private ObjectInputStream serverToClient;
    public ClientListeningThread listeningThread;
    public ClientSenderThread senderThread;

    public CommunicationManager(){
    }

    public void connect(String ip, int port) throws IOException {
        clientSocket = new Socket(ip,port);
        clientToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        serverToClient = new ObjectInputStream(clientSocket.getInputStream());
        listeningThread = new ClientListeningThread(serverToClient);
        senderThread = new ClientSenderThread(clientToServer);
    }

    public void sendMessage (JSONMessage msg){
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
    public void start(){
        listeningThread.start();
        senderThread.start();

    }

    public void waitForIt(){
        try {
            listeningThread.join();
            senderThread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void closeAll(){
        try {
            serverToClient.close();
            clientToServer.close();
            clientSocket.close();
            System.out.println("Succesfully closed all connections");
        }catch (IOException e){
            System.out.println("Closing connections unsuccesfull");
            e.printStackTrace();
        }

    }
}

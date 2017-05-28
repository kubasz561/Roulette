package communication_and_logic;

import java.io.*;
import java.net.Socket;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import logic.Overseer;


/**
 *
 */
public class ClientCommunicationThread extends Thread {
    private final Socket clientSocket;
    private final ObjectOutputStream clientToServer;
    private final ObjectInputStream serverToClient;

    /**
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public ClientCommunicationThread(String ip, int port) throws IOException
    {
        clientSocket = new Socket(ip,port);
        clientToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        serverToClient = new ObjectInputStream(clientSocket.getInputStream());
        //clientToServer.writeObject(JSONMessageBuilder.create_message("LOG_IN")); //TODO:Send login data
    }

    //TODO:New Constructor that doesn't log in but signs up



    @Override
    public void run() {
        Overseer mainOverseer = Overseer.getInstance();
        JSONMessage message;

        try  {
            while (mainOverseer.isRunningFlag){

                message = (JSONMessage) serverToClient.readObject();
                mainOverseer.comFlagSemaphore.acquireUninterruptibly();
                mainOverseer.gameStateController.handleIncomingMessage(message);
                mainOverseer.comFlagSemaphore.release();

            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error while communicating closing");
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            close_all();
        }
    }

    public void sendMessage(JSONMessage msg) throws InterruptedException, IOException {
            clientToServer.writeObject(msg);
    }

    private void close_all(){
        try {
            serverToClient.close();
            clientToServer.close();
            clientSocket.close();
        }catch (IOException e){
            System.out.println("Close unsuccesfull");
            e.printStackTrace();
        }
    }
}
package communication_and_logic;

import java.io.*;
import java.net.Socket;

import com.kubasz561.roulette.common.JSONMessage;
import logic.Overseer;


/**
 *
 */
public class ClientListeningThread extends Thread {
    private final ObjectInputStream serverToClient;


    public ClientListeningThread(ObjectInputStream stream) throws IOException
    {
        serverToClient = stream;
        //clientToServer.writeObject(JSONMessageBuilder.create_message("LOG_IN")); //TODO:Send login data
    }

    //TODO:New Constructor that doesn't log in but signs up


    @Override
    public void run() {
        Overseer mainOverseer = Overseer.getInstance();
        JSONMessage message;

        try
        {
            while (mainOverseer.isRunning)
            {
                message = (JSONMessage) serverToClient.readObject();
                mainOverseer.comFlagSemaphore.acquireUninterruptibly();
                mainOverseer.gameStateController.handleIncomingMessage(message);
                mainOverseer.comFlagSemaphore.release();
            }
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Server disconnected closing all connections");
            e.printStackTrace();
        }
        finally {
            Overseer.getInstance().communicationThread.closeAll();
        }
    }



}
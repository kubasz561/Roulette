package logic;

import com.kubasz561.roulette.common.JSONMessage;
import communication_and_logic.ClientCommunicationThread;
import logic.ClientStates;
import logic.Overseer;

import java.io.IOException;

/**
 * Created by sackhorn on 16.05.17.
 */
public class GameStateController {
    ClientStates currentState;
    Overseer mainOverseer;
    public GameStateController(Overseer overseer)
    {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = overseer;
    }


    public void handleIncomingMessage(JSONMessage msg) throws InterruptedException {
        System.out.print(msg.rawJSONString);
    }


    public boolean sendMessage(JSONMessage msg) throws IOException, InterruptedException
    {
        if(mainOverseer.comFlagSemaphore.tryAcquire())
        {
            mainOverseer.listenFlag = false;
            mainOverseer.communicationThread.sendMessage(msg);
            mainOverseer.comFlagSemaphore.release();
            return true;
        }
        else
        {
            System.out.print("Communication socket was busy when trying to send a message");
            return false;
        }
    }
    public void connect(String host, int port){
        //TODO: przenieśc to do GameStateController i LoginViewController
        ClientCommunicationThread comThread = connectToServer(host, port);
        mainOverseer.communicationThread = comThread;

        assert comThread != null;
        try {
          //  comThread.join();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private static ClientCommunicationThread connectToServer(String ip, int port)
    {
        ClientCommunicationThread newCommunicationThread;
        try
        {
            newCommunicationThread = new ClientCommunicationThread(ip,port);
            newCommunicationThread.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to connect");
            return null;
        }
        return newCommunicationThread;
    }
}

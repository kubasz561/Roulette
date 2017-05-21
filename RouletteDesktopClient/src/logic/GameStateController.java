package logic;

import com.kubasz561.roulette.common.JSONMessage;
import logic.ClientStates;
import logic.Overseer;

import java.io.IOException;

/**
 * Created by sackhorn on 16.05.17.
 */
public class GameStateController {
    ClientStates currentState;
    Overseer mainOverseer;
    public GameStateController()
    {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = Overseer.getInstance();
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
}

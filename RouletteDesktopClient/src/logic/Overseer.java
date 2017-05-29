package logic;

import communication_and_logic.ClientCommunicationThread;
import view.ConnectGUI;
import java.util.concurrent.Semaphore;

/**
 * Created by sackhorn on 16.05.17.
 */
public class Overseer {
    private static Overseer Instance;

    public boolean isRunningFlag = true;
    public Semaphore comFlagSemaphore = new Semaphore(1);
    public GameStateController gameStateController = new GameStateController(this);
    public ClientCommunicationThread communicationThread;

    private Overseer(){
    }

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }

}

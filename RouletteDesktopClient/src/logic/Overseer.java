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
    public boolean listenFlag = true; //TODO: The listenFlag May not be necessary since we are using semaphores but i will let i stay for now
    public Semaphore comFlagSemaphore = new Semaphore(1);
    public GameStateController gameStateController = new GameStateController(this);
    public ClientCommunicationThread communicationThread;
    private ConnectGUI connectGUI;

    private Overseer(){
    }

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }

}

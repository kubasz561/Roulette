package logic;

import communication_and_logic.CommunicationManager;

import java.util.concurrent.Semaphore;

/**
 * Created by sackhorn on 16.05.17.
 */
public class Overseer {
    private static Overseer Instance;

    public boolean isRunning = true;
    public Semaphore comFlagSemaphore = new Semaphore(1);
    public GameStateController gameStateController = new GameStateController(this);
    public CommunicationManager communicationThread = new CommunicationManager();

    private Overseer(){
    }

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }

}

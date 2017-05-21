package game_logic;

/**
 * Created by sackhorn on 21.05.17.
 * This class is responsible for timing when game should go to the next phase
 */
public class PhaseTimer extends Thread{
    private ServerOverseer serverOverseer = ServerOverseer.getInstance();
    private long bettingTime = 45000;
    private long rollinTime = 45000;
    private long resultsTime = 45000;


    @Override
    public void run()
    {
        try
        {
            while(serverOverseer.isRunning)
            {
                serverOverseer.serverGameLogic.changeGameState(GameState.BETTING);
                serverOverseer.serverGameLogic.sendStateUpdateToClients();
                wait(bettingTime);
                serverOverseer.serverGameLogic.changeGameState(GameState.ROLLING);
                serverOverseer.serverGameLogic.sendStateUpdateToClients();
                wait(rollinTime);
                serverOverseer.serverGameLogic.changeGameState(GameState.RESULTS);
                serverOverseer.serverGameLogic.sendStateUpdateToClients();
                wait(resultsTime);
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("InterruptedException in phaseTimer this shouldn't happen");
            e.printStackTrace();
        }


    }
}

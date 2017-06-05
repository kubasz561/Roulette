package game_logic;

import com.kubasz561.roulette.common.MessageType;

/**
 * Created by sackhorn on 21.05.17.
 * This class is responsible for timing when game should go to the next phase
 */
public class PhaseTimer extends Thread{
    private ServerOverseer serverOverseer = ServerOverseer.getInstance();
    private long bettingTime = 4500;//TODO: jakieś randowowe wartości to są pobierać resztę z configa z bazki
    private long rollinTime = 4500;
    private long resultsTime = 4500;


    @Override
    public void run()
    {
        serverOverseer.serverGameLogic.setRoundNumber(0);
        try
        {
            while(serverOverseer.isRunning)
            {
                serverOverseer.serverGameLogic.incrementRoundNumber();
                serverOverseer.serverGameLogic.changeGameState(GameState.BETTING);
                serverOverseer.serverGameLogic.sendStateUpdateToClients(MessageType.TIMESTAMP_TO_BET);
                sleep(bettingTime);
                serverOverseer.serverGameLogic.changeGameState(GameState.ROLLING);
                serverOverseer.serverGameLogic.sendStateUpdateToClients(MessageType.TIMESTAMP_TO_ROLL);
                sleep(rollinTime);
                serverOverseer.serverGameLogic.changeGameState(GameState.RESULTS);
                serverOverseer.serverGameLogic.sendStateUpdateToClients(MessageType.TIMESTAMP_TO_RESULT);
                sleep(resultsTime);
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("InterruptedException in phaseTimer this shouldn't happen");
            e.printStackTrace();
        }


    }
}

package communication;

import com.kubasz561.roulette.common.JSONMessage;
import game_logic.Bet;
import game_logic.RollResult;

import java.util.concurrent.BlockingQueue;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public ServerCommunicationThread clientComThread;
    public int clientId;
    public String passHash;
    public double accountBalance;
    public BlockingQueue<JSONMessage> outgoingQueue;
    public boolean authenticatedSuccesfully;
    private Bet currentBet;


    public Client(JSONMessage loginOrSignUpMsg, ServerCommunicationThread clientComThread, BlockingQueue<JSONMessage> outgoingQueue)
    {
        this.clientComThread = clientComThread;
        this.outgoingQueue = outgoingQueue;
        authenticateClient(loginOrSignUpMsg);
    }

    private void authenticateClient(JSONMessage loginOrSignUpMsg)
    {
        //TODO: Check if pass correct or if signup ok
        //TODO: Parse json message to fields of this class(login,password or password hash, account balance)
        authenticatedSuccesfully = true;
    }

    public void sendMessage(JSONMessage msg) {
        try
        {
            outgoingQueue.put(msg);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.out.println("Couldn't put message into outgoing queue");
        }
    }
    public void setBet(int round, int value, String color){
        currentBet.setBetRound(round);
        currentBet.setBetValue(value);
        if(color.equals("RED"))
            currentBet.setBetColor(RollResult.RED);
        if(color.equals("BLACK"))
            currentBet.setBetColor(RollResult.BLACK);
        if(color.equals("GREEN"))
            currentBet.setBetColor(RollResult.GREEN);
    }
    public void clearBet(){
        currentBet.setBetColor(null);
        currentBet.setBetRound(0);
        currentBet.setBetValue(0);
    }
    public Bet getBet(){
        return currentBet;
    }
}

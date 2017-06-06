package game_logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication.Client;

import java.io.IOException;
import java.util.Random;

/**
 * Created by sackhorn on 20.05.17.
 */
public class ServerGameLogic
{
    public PhaseTimer phaseTimer;
    private GameState currentGameState = GameState.WAITING_FOR_PLAYERS;
    private ServerOverseer serverOverseer;
    private RollResult currentResult;
    private int roundNumber;

    public ServerGameLogic(ServerOverseer overseer)
    {
        serverOverseer = overseer;
    }

    public void incrementRoundNumber(){
        roundNumber++;
    }
    public void setRoundNumber(int i){
        roundNumber = i;
    }
    public void handleMessage(JSONMessage msg, Client msgSender)
    {
        System.out.println("CLIENT_" + msgSender.clientId + ": "+ msg.getRawJSONString());
        switch(msg.getMsgType()){
            case SIGN_UP:
//                if(serverOverseer.checkDataBaseForLogin(msg.getDictionary().get("login")))
//                    serverOverseer.addNewClient(msgSender);
                break;
            case LOG_IN:

                break;
            case LOG_OUT:
                JSONMessage tmpMsg = JSONMessageBuilder.create_message(MessageType.LOG_OUT_OK);
                msgSender.sendMessage(tmpMsg);
                serverOverseer.deleteClientFromList(msgSender);
                break;
            case SET_BET:
                int betRound = Integer.parseInt(msg.getDictionary().get("session_number"));
                int betValue = Integer.parseInt(msg.getDictionary().get("value"));
                String betColor = msg.getDictionary().get("bet");
                if(currentGameState.equals(GameState.BETTING))
                {
                    if(betRound == roundNumber){

                        msgSender.setBet(betRound, betValue, betColor);
                    }

                }
                else{
                    JSONMessage tmpMsg1 = JSONMessageBuilder.create_message(MessageType.BET_UNABLE);
                    msgSender.sendMessage(tmpMsg1);
                }
                //sprawdz czy stan mozna obstawiac
                //sprawdz czy numer sesji ok
                // sprawdz w bazie czy w ystarczajaco tokenow
                // przyjmij beta
                break;
            default:
                break;
        }
    }

    public boolean checkIfBetWon(Client client){
        if(client.getBet() != null){
            if(client.getBet().getBetRound() == roundNumber && client.getBet().getBetColor().equals(currentResult))
                return true;
        }
        return false;
    }
    //Use this to change currentGameState in PhaseTimer
    public void changeGameState(GameState state)
    {

        serverOverseer.gameLogicMutex.acquireUninterruptibly();
        System.out.println(state.toString());
        currentGameState = state;
        serverOverseer.gameLogicMutex.release();
    }

    public void phaseTimer()
    {
        phaseTimer = new PhaseTimer();
        phaseTimer.start();
    }

    public void sendStateUpdateToClients(MessageType msgType)
    {
        String account;
        switch(msgType) {
            case TIMESTAMP_TO_RESULT:
                account = Integer.toString(2000); // pobranie z bazy
                currentResult = rollRoulette();
                String result = currentResult.toString();
                String stateTimeResult = Long.toString(phaseTimer.getResultsTime());
                JSONMessage tmpMsg1 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_RESULT,Integer.toString(roundNumber) ,stateTimeResult,result,account);
                serverOverseer.sendMessageToAll(tmpMsg1);
                break;
            case TIMESTAMP_TO_BET:
                account = Integer.toString(2500); // pobranie z bazy
                String stateTimeBet = Long.toString(phaseTimer.getBettingTime());
                JSONMessage tmpMsg2 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_BET, Integer.toString(roundNumber),stateTimeBet,account);
                serverOverseer.sendMessageToAll(tmpMsg2);
                break;
            case TIMESTAMP_TO_ROLL:
                String stateTimeRoll = Long.toString(phaseTimer.getRollinTime());
                JSONMessage tmpMsg3 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_ROLL, Integer.toString(roundNumber),stateTimeRoll);
                serverOverseer.sendMessageToAll(tmpMsg3);
                break;
            default:
                break;
        }
    }
    public RollResult rollRoulette(){
        RollResult[] rouletteTab = new RollResult[37];
        rouletteTab[0] = RollResult.GREEN;
        for(int i = 1; i<rouletteTab.length;i++){
            if(i % 2 == 0)
                rouletteTab[i] = RollResult.BLACK;
            else
                rouletteTab[i] = RollResult.RED;
        }
        Random rand = new Random();
        int randVal = rand.nextInt(38);
        return rouletteTab[randVal];

    }
}

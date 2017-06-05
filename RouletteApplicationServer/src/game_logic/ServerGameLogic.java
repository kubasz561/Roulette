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
                if(currentGameState.equals(GameState.BETTING))
                {
                    if(betRound == roundNumber){

                        //sprawdz na bazie
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

        switch(msgType) {
            case TIMESTAMP_TO_RESULT:
                String result = rollRoulette().toString();
                JSONMessage tmpMsg1 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_RESULT,Integer.toString(roundNumber) ,"45",result,"1200");
                serverOverseer.sendMessageToAll(tmpMsg1);
                break;
            case TIMESTAMP_TO_BET:
                JSONMessage tmpMsg2 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_BET, Integer.toString(roundNumber),"45","1500");
                serverOverseer.sendMessageToAll(tmpMsg2);
                break;
            case TIMESTAMP_TO_ROLL:
                JSONMessage tmpMsg3 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_ROLL, Integer.toString(roundNumber),"30");
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

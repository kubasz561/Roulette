package game_logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication.Client;

import java.io.IOException;
import java.util.Random;

import static com.kubasz561.roulette.common.MessageType.BET_LOST;
import static com.kubasz561.roulette.common.MessageType.BET_WON;

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
                String newLogin = msg.getDictionary().get("login");
                String newPassword = msg.getDictionary().get("password");

                UserDTO searchUserResult = serverOverseer.databaseClient.searchUser(newLogin);
                if(searchUserResult.isPresent()){
                    msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.LOGIN_DUPLICATE));
                }
                else {
                    if(serverOverseer.databaseClient.createUser(newLogin, newPassword)){
                        msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.SIGN_UP_OK, Integer.toString(serverOverseer.databaseClient.getClientAccount(msgSender))));
                        msgSender.authenticatedSuccesfully = true;
                        serverOverseer.addNewClient(msgSender);
                    }
                    else {
                        msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.SIGN_UP_UNABLE));
                    }
                }
                break;
            case LOG_IN:
                String login = msg.getDictionary().get("login");
                String password = msg.getDictionary().get("password");
                UserDTO searchUserResult2 = serverOverseer.databaseClient.searchUser(login);
                if( ! searchUserResult2.isPresent()){
                    msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.LOGIN_INVALID));
                }
                else if ( searchUserResult2.isBlocked()){
                    msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.BLOCKED));
                }
                else if ( searchUserResult2.getPassword().equals(password) ){
                    msgSender.authenticatedSuccesfully = true;
                    serverOverseer.addNewClient(msgSender);
                    msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.LOGIN_OK, Integer.toString(serverOverseer.databaseClient.getClientAccount(msgSender))));
                }
                else {
                    msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.WRONG_PASS));
                }
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

                if(currentGameState.equals(GameState.BETTING)) {
                    if(betRound == roundNumber){

                        msgSender.setBet(betRound, betValue, betColor);
                    }
                    else {
                        msgSender.sendMessage(JSONMessageBuilder.create_message(MessageType.BAD_SESSION_ID));
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
    public void checkIfClientsWonBet() {
        for (Client client : serverOverseer.clientList) {
            if (client.getBet() != null) {
                if (client.getBet().getBetRound() != 0) {
                    if (checkIfBetWon(client)) {
                        serverOverseer.databaseClient.updateClientAccount(client, getBetMultiplier()*client.getBet().getBetValue());
                        JSONMessage betWon = JSONMessageBuilder.create_message(BET_WON, Integer.toString(serverOverseer.databaseClient.getClientAccount(client)));
                        client.sendMessage(betWon);
                    } else {
                        serverOverseer.databaseClient.updateClientAccount(client, -getBetMultiplier()*client.getBet().getBetValue());
                        JSONMessage betLost = JSONMessageBuilder.create_message(BET_LOST , Integer.toString(serverOverseer.databaseClient.getClientAccount(client)));
                        client.sendMessage(betLost);
                    }
                }
            }
        }
    }
    public void resetBets(){
        for(Client client : serverOverseer.clientList)
        {
            client.clearBet();
        }
    }
    public int getBetMultiplier(){
        if(currentResult.equals(RollResult.RED) || currentResult.equals(RollResult.BLACK))
            return 1;
        if(currentResult.equals(RollResult.GREEN))
            return 13;
        return 0;
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

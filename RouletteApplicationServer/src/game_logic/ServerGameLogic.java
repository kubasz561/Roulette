package game_logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication.Client;

import java.io.IOException;

/**
 * Created by sackhorn on 20.05.17.
 */
public class ServerGameLogic
{
    public PhaseTimer phaseTimer;
    private GameState currentGameState = GameState.WAITING_FOR_PLAYERS;
    private ServerOverseer serverOverseer;

    public ServerGameLogic(ServerOverseer overseer)
    {
        serverOverseer = overseer;
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
                // close connection with that client
                //serverOverseer.get

                break;
            case SET_BET:

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
                JSONMessage tmpMsg1 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_RESULT, "11:58:13","45","13_BLACK","1200");
                serverOverseer.sendMessageToAll(tmpMsg1);
                break;
            case TIMESTAMP_TO_BET:
                JSONMessage tmpMsg2 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_BET, "12:22:47","45","1500");
                serverOverseer.sendMessageToAll(tmpMsg2);
                break;
            case TIMESTAMP_TO_ROLL:
                JSONMessage tmpMsg3 = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_ROLL, "21:57:05","30");
                serverOverseer.sendMessageToAll(tmpMsg3);
                break;
            default:
                break;
        }
    }
}

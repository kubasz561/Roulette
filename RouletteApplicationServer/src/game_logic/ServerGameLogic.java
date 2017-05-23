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

    public void handleMessage(JSONMessage msg)
    {
        switch(msg.getMsgType()){
            case SIGN_UP:
                String userLogin = msg.getDictionary().get("login");
                String userPassword = msg.getDictionary().get("password");

                /*wywołanie funkcji sprawdzającej z logowanie w bazie
                * loginAccepted = serverOverseer.database.checkLogin(userLogin, userPassword);
                * */
                boolean loginAccepted = true; //do pobrania
               // if(loginAccepted)
                // tu dodajemy wskazanie na klienta w sendmessage
                  //  serverOverseer.sendMessage(findClient(userLogin), SIGN_UP_OK);
              //  else
                //    serverOverseer.sendMessage
                break;
            case LOG_IN:

                break;

            case LOG_OUT:

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
        currentGameState = state;
        serverOverseer.gameLogicMutex.release();
    }

    public void phaseTimer()
    {
        phaseTimer = new PhaseTimer();
        phaseTimer.run();
    }

    public void sendStateUpdateToClients()
    {
        for(Client client: serverOverseer.clientList)
        {
            //TODO: Build a message with a timestamp to next round and data about round
            JSONMessage tmpMsg = JSONMessageBuilder.create_message(MessageType.TIMESTAMP_TO_RESULT,"Kaczka");
            try {
                client.thisClientComThread.sendMessage(tmpMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

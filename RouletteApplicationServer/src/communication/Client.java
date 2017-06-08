package communication;

import com.kubasz561.roulette.common.JSONMessage;
import game_logic.Bet;
import game_logic.RollResult;
import com.kubasz561.roulette.common.MessageType;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public CommunicationManagerServer clientComThread;
    public int clientId;
    private String login;
    public int accountBalance;
    public boolean authenticatedSuccesfully;
    private Bet currentBet;


    public Client(Socket newClientSocket){
        clientComThread = new CommunicationManagerServer(newClientSocket, this);
        clientComThread.start();
    }

    public MessageType authenticateClient(JSONMessage loginOrSignUpMsg)
    {
        //TODO: Check if pass correct or if signup ok
        //TODO: Parse json message to fields of this class(login,password or password hash, account balance)
        authenticatedSuccesfully = true;
        return MessageType.LOGIN_OK;
    }

    public void sendMessage(JSONMessage msg) {

            clientComThread.sendMessage(msg);

    }

    public void setBet(int round, int value, String color){
       currentBet = new Bet(round, value, color);
    }
    public void clearBet(){
      currentBet = null;
    }
    public Bet getBet(){
        return currentBet;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}

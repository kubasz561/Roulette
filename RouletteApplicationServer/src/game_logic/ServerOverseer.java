package game_logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import communication.Client;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import static com.kubasz561.roulette.common.MessageType.*;

/**
 * Created by sackhorn on 20.05.17.
 */
public class ServerOverseer {
    public ArrayList<Client> clientList = new ArrayList<>();
    public int clientsNmbr = 0;
    public Semaphore gameLogicMutex = new Semaphore(1);
    public ServerGameLogic serverGameLogic = new ServerGameLogic(this);
    public boolean isRunning = true;

    private static ServerOverseer Instance;

    private ServerOverseer() {
    }

    public static ServerOverseer getInstance() {
        if (Instance == null)
            Instance = new ServerOverseer();
        return Instance;
    }

    public void deleteClientFromList(Client clientToFind) {
        //mutex na dostep do clientlist

        for (int i = 0; i < clientList.size(); i++)
            if (clientList.get(i).equals(clientToFind))
                clientList.remove(i);


    }

    public void addNewClient(Client client) {//mutex tutaj na dostep do clientlist
        clientsNmbr++;
        clientList.add(client);
        if (clientsNmbr == 1)
            serverGameLogic.phaseTimer(); //Starting the betting round fix this up and uncomment
    }

    public void sendMessageToAll(JSONMessage msg) {//mutex tez na clientlist
        for (Client client : clientList) {
            client.sendMessage(msg);
        }
    }

    public void checkIfClientsWonBet() {
        for (Client client : clientList) {
            if (client.getBet() != null) {
                if (client.getBet().getBetRound() != 0) {
                    if (serverGameLogic.checkIfBetWon(client) == true) {
                        JSONMessage betWon = JSONMessageBuilder.create_message(BET_WON);
                        client.sendMessage(betWon);
                    } else {
                        JSONMessage betLost = JSONMessageBuilder.create_message(BET_LOST);
                        client.sendMessage(betLost);
                    }
                }
            }
        }
    }
    public void resetBets(){
        for(Client client : clientList)
        {
            client.clearBet();
        }
    }

}

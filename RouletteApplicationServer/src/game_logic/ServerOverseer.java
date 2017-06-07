package game_logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import communication.Client;

import java.sql.Connection;
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
    public DatabaseClient databaseClient;

    private static ServerOverseer Instance;
    public Connection dbConnection;

    private ServerOverseer() {
    }

    public static ServerOverseer getInstance() {
        if (Instance == null)
            Instance = new ServerOverseer();
        return Instance;
    }

    public void deleteClientFromList(Client clientToFind){
        ///mutex from handleMessage
        if(clientList.removeIf(client -> client.equals(clientToFind)))
            --clientsNmbr;
    }

    public void addNewClient(Client client)
    {//mutex from handleMessage
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
}

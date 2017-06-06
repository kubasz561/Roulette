package communication;

import com.kubasz561.roulette.common.JSONMessage;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public CommunicationManagerServer clientComThread;
    public int clientId;
    public String passHash;
    public double accountBalance;
    public BlockingQueue<JSONMessage> outgoingQueue;
    public boolean authenticatedSuccesfully;


    public Client(Socket newClientSocket){
        this.clientComThread = new CommunicationManagerServer(newClientSocket);
        authenticateClient(loginOrSignUpMsg);
    }

    public void authenticateClient(JSONMessage loginOrSignUpMsg)
    {
        //TODO: Check if pass correct or if signup ok
        //TODO: Parse json message to fields of this class(login,password or password hash, account balance)
        authenticatedSuccesfully = true;
    }

    public void sendMessage(JSONMessage msg) {

            clientComThread.sendMessage(msg);

    }
}

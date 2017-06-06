package communication;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.MessageType;

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
}

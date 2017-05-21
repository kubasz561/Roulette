package communication;

import com.kubasz561.roulette.common.JSONMessage;

import java.util.ArrayList;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public ServerCommunicationThread thisClientComThread;
    public int clientId;
    public String passHash;
    public double accountBalance;

    public Client(JSONMessage logInMessage)
    {
        //TODO:Parse json login message to fields of this class
    }

    public boolean authenticateClient()
    {
        //TODO: Check if pass correct
        return true;
    }
}

package communication;

import com.kubasz561.roulette.common.JSONMessage;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public ServerCommunicationThread thisClientComThread;
    public int clientId;
    public String login;
    public String passHash;
    public double accountBalance;

    public Client(JSONMessage logInMessage)
    {
        logInMessage.parseJSONString(logInMessage.getRawJSONString());
        this.login = logInMessage.getDictionary().get("login");
        this.passHash = logInMessage.getDictionary().get("password");
        //this.accountBalance; - > do pobrania z bazy

    }

    public boolean authenticateClient()
    {
        //TODO: Check if pass correct
        return true;
    }
    public void setClientId(int id){
        this.clientId = id;
    }
}

package communication;

import com.kubasz561.roulette.common.JSONMessage;

/**
 * Created by sackhorn on 20.05.17.
 */
public class Client {
    public ServerCommunicationThread clientComThread;
    public int clientId;
    public String clientLogin;
    public String passHash;
    public double accountBalance;
    public boolean authenticatedSuccesfully;

    public Client(JSONMessage loginOrSignUpMsg, ServerCommunicationThread clientComThread)
    {
        this.clientComThread = clientComThread;
        this.clientId = clientComThread.serverOverseer.clientList.size();
        authenticateClient(loginOrSignUpMsg);
        this.clientLogin = loginOrSignUpMsg.getDictionary().get("login");
        this.passHash = loginOrSignUpMsg.getDictionary().get("password");
        this.accountBalance = 200; //odwołanie do bazy danych, które zwraca wartość dla danego loginu i hasla
    }

    private void authenticateClient(JSONMessage loginOrSignUpMsg)
    {
        //TODO: Check if pass correct or if signup ok
        //TODO: Parse json message to fields of this class(login,password or password hash, account balance)
        this.authenticatedSuccesfully = true;
    }
}

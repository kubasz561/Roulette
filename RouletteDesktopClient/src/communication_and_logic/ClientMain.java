package communication_and_logic;

import logic.GameStateController;
import logic.Overseer;
import view.ConnectView;
import view_controllers.LoginViewController;
import java.io.*;

public class ClientMain {

    public static void main(String[] args) throws IOException, InterruptedException
    {

        //TODO: usunąć to i zamiast tego dodać pola w ConnectView
        String ip = "127.0.0.1";
        int port = 1234;
        System.out.println("Trying to connect to: " + ip + " " + port);

        Overseer mainOverseer = Overseer.getInstance();

        //TODO: przenieśc to do GameStateController i LoginViewController
        ClientCommunicationThread comThread = connectToServer(ip, port);
        mainOverseer.communicationThread = comThread;

        assert comThread != null;
        comThread.join();
    }

    private static ClientCommunicationThread connectToServer(String ip, int port)
    {
        ClientCommunicationThread newCommunicationThread;
        try
        {
            newCommunicationThread = new ClientCommunicationThread(ip,port);
            newCommunicationThread.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to connect");
            return null;
        }
        return newCommunicationThread;
    }
}

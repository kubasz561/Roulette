package logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication_and_logic.ClientCommunicationThread;
import view.ConnectGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by sackhorn on 16.05.17.
 */
public class GameStateController {
    ClientStates currentState;
    Overseer mainOverseer;
    ConnectGUI connectGUI;
    public GameStateController(Overseer overseer)
    {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = overseer;
        connectGUI = new ConnectGUI();
        connectGUI.addLoginActionListener(new LoginActionListener());
        connectGUI.addSignUpActionListener(new SignUpActionListener());
    }

    class SignUpActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            connect(connectGUI.getHost(), connectGUI.getPort());
            JSONMessage loginMsg = JSONMessageBuilder.create_message(MessageType.SIGN_UP,connectGUI.getLogin(), connectGUI.getPassword());
            try{
                sendMessage(loginMsg);
            } catch (Exception e) {
                e.printStackTrace(); //TODO: Handlowac tym glebiej
            }
        }
    }
    class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            connect(connectGUI.getHost(), connectGUI.getPort());
            JSONMessage loginMsg = JSONMessageBuilder.create_message(MessageType.LOG_IN,connectGUI.getLogin(), connectGUI.getPassword());
            try{
               sendMessage(loginMsg);
            } catch (Exception e) {
                e.printStackTrace(); //TODO: Handlowac tym glebiej
            }
        }
    }

    public void handleIncomingMessage(JSONMessage msg) throws InterruptedException {
        System.out.print(msg.rawJSONString);
    }


    public boolean sendMessage(JSONMessage msg) throws IOException, InterruptedException
    {
        if(mainOverseer.comFlagSemaphore.tryAcquire())
        {
            mainOverseer.listenFlag = false;
            mainOverseer.communicationThread.sendMessage(msg);
            mainOverseer.comFlagSemaphore.release();
            return true;
        }
        else
        {
            System.out.print("Communication socket was busy when trying to send a message");
            return false;
        }
    }
    public void connect(String host, int port){
        ClientCommunicationThread comThread = connectToServer(host, port);
        mainOverseer.communicationThread = comThread;

        assert comThread != null;
        try {
          //  comThread.join();

        } catch (Exception e){
            e.printStackTrace();
        }
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

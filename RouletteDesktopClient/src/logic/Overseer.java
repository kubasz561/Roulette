package logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication_and_logic.ClientCommunicationThread;
import view.ConnectGUI;
import view.LoginView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

/**
 * Created by sackhorn on 16.05.17.
 */
public class Overseer {
    private static Overseer Instance;

    public boolean isRunningFlag = true;
    public boolean listenFlag = true; //TODO: The listenFlag May not be necessary since we are using semaphores but i will let i stay for now
    public Semaphore comFlagSemaphore = new Semaphore(1);
    public GameStateController gameStateController = new GameStateController(this);
    public ClientCommunicationThread communicationThread;
    private ConnectGUI connectGUI;
    private LoginView loginView;

    private Overseer(){
        connectGUI = new ConnectGUI();
        loginView = new LoginView();
        connectGUI.addActionListener(new ConnectActionListener());
        connectGUI.init();
    }

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }


    class ConnectActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            gameStateController.connect(connectGUI.getHost(), connectGUI.getPort());
            loginView.addActionListener(new LoginActionListener());
            loginView.init();
        }
    }
    class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JSONMessage loginMsg = JSONMessageBuilder.create_message(MessageType.LOG_IN,loginView.getLogin(), loginView.getPassword());
            try{
                gameStateController.sendMessage(loginMsg);
            } catch (Exception e) {
                e.printStackTrace(); //TODO: Handlowac tym glebiej
            }
        }
    }
}

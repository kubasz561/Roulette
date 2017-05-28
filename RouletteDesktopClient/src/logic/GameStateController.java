package logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
import communication_and_logic.ClientCommunicationThread;
import view.BettingGUI;
import view.ConnectGUI;

import javax.swing.*;
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
    BettingGUI bettingGUI;
    String clientLogin;
    String clientPassword;

    public GameStateController(Overseer overseer) {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = overseer;
        connectGUI = new ConnectGUI();
        connectGUI.addLoginActionListener(new LoginActionListener());
        connectGUI.addSignUpActionListener(new SignUpActionListener());
    }

    class SignUpActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            connect(connectGUI.getHost(), connectGUI.getPort());
            JSONMessage loginMsg = JSONMessageBuilder.create_message(MessageType.SIGN_UP,connectGUI.getLogin(), connectGUI.getPassword());
            try{
                sendMessage(loginMsg);
            } catch (Exception e) {
                e.printStackTrace(); //TODO: Handlowac tym glebiej
            }
            mainOverseer.comFlagSemaphore.release();
        }
    }
    class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            connect(connectGUI.getHost(), connectGUI.getPort());
            //tu trzeba walidację jakąś zrobić
            JSONMessage loginMsg = JSONMessageBuilder.create_message(MessageType.LOG_IN,connectGUI.getLogin(), connectGUI.getPassword());
            try{
                sendMessage(loginMsg);
            } catch (Exception e) {
                e.printStackTrace(); //TODO: Handlowac tym glebiej
            }
            mainOverseer.comFlagSemaphore.release();
        }
    }
    class BetActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            if(bettingGUI.getBetAmount().isEmpty())
                 return;
            if(bettingGUI.getAccountValue() < bettingGUI.getBetAmountValue())
                return;
            JSONMessage betMsg = JSONMessageBuilder.create_message(MessageType.SET_BET,clientLogin,"red" ,bettingGUI.getBetAmount(),"session1234" ,connectGUI.getPassword());
            try{
                sendMessage(betMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bettingGUI.lockBettingGUI(); // lockuje przycisk do betowania az nie ..
                                        // dostaniemy odpowiedzi czy bet ok, zeby nie slac bez sensu duzo komunikatow
            mainOverseer.comFlagSemaphore.release();
        }
    }


    public void handleIncomingMessage(JSONMessage msg) throws InterruptedException {
        System.out.print(msg.rawJSONString);
        switch(msg.getMsgType()){
            case SIGN_UP_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                break;

            case LOGIN_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                break;

            case LOGIN_DUPLICATE:
                // tego chyba wgl nie bedzie skoro jesli logowanie i rejestracja tak samo działaja
                break;

            case BET_OK:
                // tu coś w widoku trzeba będzie pokazać
               // bettingGUI.set
                break;
            case WRONG_PASS:
                JOptionPane.showMessageDialog(bettingGUI,
                        "Wrong password for given Login.");
                connectGUI.clearPassword();
                break;
            case BLOCKED:
                JOptionPane.showMessageDialog(bettingGUI,
                        "User blocked");
                break;
            case BET_UNABLE:
                bettingGUI.unlockBettingGUI();
                JOptionPane.showMessageDialog(bettingGUI,
                        "Unable to bet.");
                break;
            case TIMESTAMP_TO_BET:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_BET);
                bettingGUI.unlockBettingGUI();
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                //odblokować wpisywanie wartosci betu i przycisk betowania
                break;
            case TIMESTAMP_TO_RESULT:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_RESULT);
                bettingGUI.setResultLabel(msg.getDictionary().get("result"));
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                //wyswietlić wynik losowania
                break;
            case TIMESTAMP_TO_ROLL:
                bettingGUI.lockBettingGUI();
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_ROLL);
                //zablokowac wpisywanie wartosci betu i przycisk betowania + Licznik
                break;
            default:
                break;
        }
    }

    /**
     *  Called only from GUI, semaphore acquire in listener
     */
    public boolean sendMessage(JSONMessage msg) throws IOException, InterruptedException {
            mainOverseer.communicationThread.sendMessage(msg);
            return true;
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
    private static ClientCommunicationThread connectToServer(String ip, int port) {
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
    private void changeLogToGameFrame(){
        bettingGUI = new BettingGUI();
        bettingGUI.addBetActionListener(new BetActionListener());
        connectGUI.setVisible(false);
    }
}

package logic;

import com.kubasz561.roulette.common.JSONMessage;
import com.kubasz561.roulette.common.JSONMessageBuilder;
import com.kubasz561.roulette.common.MessageType;
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
    int sessionNuber;

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
    class BetGreenActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            if(bettingGUI.getBetAmount().isEmpty())
                 return;
            if(bettingGUI.getAccountValue() < bettingGUI.getBetAmountValue() || bettingGUI.getBetAmountValue() <= 0 )
                return;
            JSONMessage betMsg = JSONMessageBuilder.create_message(MessageType.SET_BET,"GREEN" ,bettingGUI.getBetAmount(),Integer.toString(sessionNuber));
            try{
                sendMessage(betMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bettingGUI.setAccountLabel(Integer.toString(bettingGUI.getAccountValue() - Integer.parseInt(bettingGUI.getBetAmount())));
            bettingGUI.lockBettingGUI(); // lockuje przycisk do betowania az nie ..
                                        // dostaniemy odpowiedzi czy bet ok, zeby nie slac bez sensu duzo komunikatow
            mainOverseer.comFlagSemaphore.release();
        }
    }
    class BetRedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            if(bettingGUI.getBetAmount().isEmpty())
                return;
            if(bettingGUI.getAccountValue() < bettingGUI.getBetAmountValue() || bettingGUI.getBetAmountValue() <= 0 )
                return;
            JSONMessage betMsg = JSONMessageBuilder.create_message(MessageType.SET_BET,"RED" ,bettingGUI.getBetAmount(),Integer.toString(sessionNuber));
            try{
                sendMessage(betMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bettingGUI.setAccountLabel(Integer.toString(bettingGUI.getAccountValue() - Integer.parseInt(bettingGUI.getBetAmount())));
            bettingGUI.lockBettingGUI(); // lockuje przycisk do betowania az nie ..
            // dostaniemy odpowiedzi czy bet ok, zeby nie slac bez sensu duzo komunikatow
            mainOverseer.comFlagSemaphore.release();
        }
    }
    class BetBlackActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            if(bettingGUI.getBetAmount().isEmpty())
                return;
            if(bettingGUI.getAccountValue() < bettingGUI.getBetAmountValue() || bettingGUI.getBetAmountValue() <= 0 )
                return;
            JSONMessage betMsg = JSONMessageBuilder.create_message(MessageType.SET_BET,"BLACK" ,bettingGUI.getBetAmount(),Integer.toString(sessionNuber));
            try{
                sendMessage(betMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bettingGUI.setAccountLabel(Integer.toString(bettingGUI.getAccountValue() - Integer.parseInt(bettingGUI.getBetAmount())));
            bettingGUI.lockBettingGUI(); // lockuje przycisk do betowania az nie ..
            // dostaniemy odpowiedzi czy bet ok, zeby nie slac bez sensu duzo komunikatow
            mainOverseer.comFlagSemaphore.release();
        }
    }
    class LogoutActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            mainOverseer.comFlagSemaphore.tryAcquire();
            //logout
            JSONMessage betMsg = JSONMessageBuilder.create_message(MessageType.LOG_OUT);
            try{
                sendMessage(betMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            changeGameToLogFrame(); //TODO to delete after receiving response from server
            mainOverseer.comFlagSemaphore.release();
        }
    }

    public void handleIncomingMessage(JSONMessage msg) throws InterruptedException {
        System.out.println("SERVER: " + msg.rawJSONString);
        switch(msg.getMsgType()){
            case SIGN_UP_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                break;

            case LOGIN_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                break;
            case LOGIN_INVALID:
                JOptionPane.showMessageDialog(bettingGUI,
                        "User doesn't exist");
                connectGUI.clearPassword();
                break;
            case SIGN_UP_UNABLE:
                JOptionPane.showMessageDialog(bettingGUI,
                        "Unable to sign up");
                break;

            case LOG_OUT_OK:

                changeGameToLogFrame();
                JOptionPane.showMessageDialog(connectGUI,
                        "Log out successful");
                break;

            case BET_WON:
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                JOptionPane.showMessageDialog(bettingGUI,
                        "You won the bet");
                break;
            case BET_LOST:
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                JOptionPane.showMessageDialog(bettingGUI,
                        "You lost the bet");
                break;
            case BET_OK:
                bettingGUI.setBetResult(MessageType.BET_OK);
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
                bettingGUI.setBetResult(MessageType.BET_UNABLE);
                JOptionPane.showMessageDialog(bettingGUI,
                        "Unable to bet.");
                break;
            case BAD_SESSION_ID:
                bettingGUI.unlockBettingGUI();
                bettingGUI.setBetResult(MessageType.BAD_SESSION_ID);
                break;
            case TIMESTAMP_TO_BET:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_BET);
                bettingGUI.clearBetResult();
                bettingGUI.unlockBettingGUI();
                //bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                sessionNuber = Integer.parseInt(msg.getDictionary().get("timestamp"));
                bettingGUI.setResultLabel("");
                //odblokować wpisywanie wartosci betu i przycisk betowania
                break;
            case TIMESTAMP_TO_RESULT:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_RESULT);
                bettingGUI.clearBetResult();
                bettingGUI.setResultLabel(msg.getDictionary().get("result"));
                //bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance")); // nie wiem czemu tu nie wyciaga nic
                sessionNuber = Integer.parseInt(msg.getDictionary().get("timestamp"));
                //wyswietlić wynik losowania
                break;
            case TIMESTAMP_TO_ROLL:
                bettingGUI.lockBettingGUI();
                bettingGUI.clearBetResult();
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_ROLL);
                sessionNuber = Integer.parseInt(msg.getDictionary().get("timestamp"));
                bettingGUI.setResultLabel("");
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
        try {
            mainOverseer.communicationThread.connect(host, port);

        } catch (IOException e){
            e.printStackTrace();
        }
        mainOverseer.communicationThread.start();

    }

    private void changeLogToGameFrame(){
        bettingGUI = new BettingGUI();
        bettingGUI.addBetGreenActionListener(new BetGreenActionListener());
        bettingGUI.addBetRedActionListener(new BetRedActionListener());
        bettingGUI.addBetBlackActionListener(new BetBlackActionListener());
        bettingGUI.addLogoutActionListener(new LogoutActionListener());
        connectGUI.setVisible(false);
    }
    private void changeGameToLogFrame(){
        bettingGUI.setVisible(false);
        connectGUI.setVisible(true);
    }
}

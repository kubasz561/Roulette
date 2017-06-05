package view;

import com.kubasz561.roulette.common.MessageType;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Betting GUI
 */
public class BettingGUI extends JFrame{
    private JPanel panel;
    private JButton betGreenButton;
    private JTextField betAmount;
    private JLabel enterTokensLabel;
    private JLabel gameStateInfo;
    private JLabel resultLabel;
    private JLabel accountLabel;
    private JLabel pictureLabel;
    private JLabel betResult;
    private JButton betBlackButton;
    private JButton betRedButton;
    private JButton logoutButton;


    public BettingGUI() {
        this.setContentPane(panel);
        this.setName("Roulette");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.betGreenButton.setEnabled(true);
        this.betAmount.setEnabled(true);
        this.setVisible(true);
    }
    public void addBetGreenActionListener(ActionListener actionListener){
        betGreenButton.addActionListener(actionListener);
    }
    public void addBetBlackActionListener(ActionListener actionListener){
        betBlackButton.addActionListener(actionListener);
    }
    public void addBetRedActionListener(ActionListener actionListener){
        betRedButton.addActionListener(actionListener);
    }
    public void addLogoutActionListener(ActionListener actionListener){
        logoutButton.addActionListener(actionListener);
    }


    public String getBetAmount(){
        return betAmount.getText();
    }
    public int getBetAmountValue(){
        return Integer.parseInt(betAmount.getText());
    }

    public void clearBetAmountTextField(){
        betAmount.setText("");
    }
    public void setGameStateInfoLabel(MessageType type){
        switch(type){
            case TIMESTAMP_TO_BET:
                gameStateInfo.setText("Time to bet..");
                break;
            case TIMESTAMP_TO_RESULT:
                gameStateInfo.setText("Results:");
                break;

            case TIMESTAMP_TO_ROLL:
                gameStateInfo.setText("Rolling...");
                break;
        }
    }
    public void lockBettingGUI(){
        betGreenButton.setEnabled(false);
        betRedButton.setEnabled(false);
        betBlackButton.setEnabled(false);
        betAmount.setEnabled(false);
    }
    public void unlockBettingGUI(){
        betGreenButton.setEnabled(true);
        betRedButton.setEnabled(true);
        betBlackButton.setEnabled(true);
        betAmount.setEnabled(true);
    }
    public int getAccountValue(){
       return Integer.parseInt(accountLabel.getText());
    }
    public void setAccountLabel(String result){
        accountLabel.setText(result);
    }
    public void setResultLabel(String result){
        if(result.equals("win"))
            resultLabel.setText("You WON");
        if(result.equals("lose"))
            resultLabel.setText("You LOST...");
    }
    public void setBetResult(MessageType type) {
        switch (type) {
            case BET_OK:
                betResult.setText("BET OK");
                break;
            case BAD_SESSION_ID:
                betResult.setText("WRONG SESSION");
                break;
            case BET_UNABLE:
                betResult.setText("UNABLE TO BET, TRY AGAIN");
                break;
            default:
                clearBetResult();
                break;
        }
    }

    public void clearBetResult() {
        betResult.setText("");
    }
}

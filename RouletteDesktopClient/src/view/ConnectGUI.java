package view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Connect view
 */
public class ConnectGUI extends JFrame{
    private JTextField hostField;
    private JTextField portField;
    private JButton loginButton;
    private JPanel panel;
    private JTextField passwordField;
    private JButton signupButton;
    private JTextField loginField;

    public ConnectGUI() {
        this.setContentPane(panel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    public void addLoginActionListener(ActionListener actionListener){
        loginButton.addActionListener(actionListener);
    }
    public void addSignUpActionListener(ActionListener actionListener){
        signupButton.addActionListener(actionListener);
    }
    public String getHost(){
        return hostField.getText();
    }
    public int getPort(){
        return Integer.parseInt(portField.getText());
    }
    public String getLogin(){
        return loginField.getText();
    }
    public String getPassword(){
        return passwordField.getText();
    }

}

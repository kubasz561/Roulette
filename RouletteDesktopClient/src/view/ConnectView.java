package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by sackhorn on 16.05.17.
 */
public class ConnectView extends JFrame {

    private LoginFrame loginFrame;
    //private SignUpFrame signUpFrame;

    private JPanel panel = new JPanel();
    private JButton login = new JButton("Login");
    private JButton signUp = new JButton("Sign Up");
    private JButton exit = new JButton("Exit");



    public ConnectView() {
        setTitle("Roulette");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.add(login);
        panel.add(signUp);
        panel.add(exit);
        panel.setLayout(new GridLayout(3, 1));
        this.setLayout(new GridLayout(0, 1));
        pack();
        setVisible(true);


    }
    public LoginFrame getLoginFrame() {
        return loginFrame;
    }
    public void setConnectLoginButtonListener(ActionListener listener) {
        login.addActionListener(listener);
    }
    public void setConnectSingUpButtonListener(ActionListener listener) {
        signUp.addActionListener(listener);
    }
    public void setConnectExitButtonListener(ActionListener listener) {
        login.addActionListener(listener);
    }
}

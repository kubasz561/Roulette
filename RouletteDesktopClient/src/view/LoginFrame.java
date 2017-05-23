package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Kuba on 2017-05-23.
 */
public class LoginFrame extends JFrame {

    private String login;
    private String password;

    private JPanel panel = new JPanel();
    private JButton loginButton = new JButton("Login");
    private JButton exitButton = new JButton("Exit");
    private JTextField loginInput = new JTextField(3);
    private JTextField passwordInput = new JTextField(3);
    private JLabel loginLabel = new JLabel("Login");
    private JLabel passwordLabel = new JLabel("Password");

    public LoginFrame(){
        setTitle("Login Dialog");
        setVisible(true);
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel.add(loginLabel);
        panel.add(loginInput);
        panel.add(passwordLabel);
        panel.add(passwordInput);
        panel.setLayout(new GridLayout(2,2));

        loginInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login = loginInput.getText();
            }
        });
        passwordInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                password = passwordInput.getText();
            }
        });

        panel.add(loginButton);
        panel.add(exitButton);
    }
    public void setLoginFrameLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }
    public void setLoginFrameExitButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

}

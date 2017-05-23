package view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by HP on 2017-05-24.
 */
public class LoginView {
    private JPanel panel;
    private JButton logInButton;
    private JTextField textField1;
    private JTextField textField2;

    public void addActionListener(ActionListener actionListener){
        logInButton.addActionListener(actionListener);
    }
    public String getLogin(){
        return textField1.getText();
    }
    public String getPassword(){
        return textField2.getText();
    }
    public JFrame init(){
        JFrame frame = new JFrame("Login");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

}

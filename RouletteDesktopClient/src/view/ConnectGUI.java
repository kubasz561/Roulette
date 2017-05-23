package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by HP on 2017-05-23.
 */
public class ConnectGUI {
    private JTextField textField1;
    private JTextField textField2;
    private JButton connectButton;
    private JPanel panel;

    public ConnectGUI() {
    }

    public JFrame init(){
        JFrame frame = new JFrame("Connect");
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    public void addActionListener(ActionListener actionListener){
        connectButton.addActionListener(actionListener);
    }
    public String getHost(){
        return textField1.getText();
    }
    public int getPort(){
        return Integer.parseInt(textField2.getText());
    }

}

package view_controllers;

import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sackhorn on 16.05.17.
 */
public class LoginViewController extends ViewController {

    private MainView theView;

    public LoginViewController(MainView mainView){
        this.theView = mainView;
        this.theView.setConnectLoginButtonListener(new LoginListener());
        this.theView.setConnectSignUpButtonListener(new SignUpListener());
        this.theView.setConnectExitButtonListener(new ExitListener());

    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            theView.initLoginFrame();
           // theView.getConnectView().getLoginFrame()

        }
    }

    private class SignUpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}

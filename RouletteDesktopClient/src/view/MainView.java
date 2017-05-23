package view;

import java.awt.event.ActionListener;

/**
 * Created by Kuba on 2017-05-23.
 */
public class MainView {

    private ConnectView connectView;
    private TableView tableView;

    public MainView(){
        connectView = new ConnectView();
    }

    public void setConnectLoginButtonListener(ActionListener listener){
        connectView.setConnectLoginButtonListener(listener);
    }
    public void setConnectSignUpButtonListener(ActionListener listener){
        connectView.setConnectSingUpButtonListener(listener);
    }
    public void setConnectExitButtonListener(ActionListener listener){
        connectView.setConnectExitButtonListener(listener);
    }
    public void initLoginFrame(){
        //connectView.
    }
    public ConnectView getConnectView() {
        return connectView;
    }

    public TableView getTableView() {
        return tableView;
    }
}

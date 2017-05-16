package com.kubasz561.roulette.desktopclient.controller;

import view_controllers.ViewController;

import javax.swing.*;
import java.util.concurrent.Semaphore;

/**
 * Created by sackhorn on 16.05.17.
 */
public class Overseer {
    public static Overseer Instance;

    public boolean isRunningFlag = true;
    public boolean listenFlag = true;
    public boolean sendFlag = true;
    Semaphore comFlagSemaphore = new Semaphore(1);
    JFrame currentView;
    ViewController currentViewController;
    GameStateController gameStateController;
    ClientCommunicationThread communicationThread;

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }

}

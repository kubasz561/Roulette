package com.kubasz561.roulette.desktopclient.controller;

import com.kubasz561.roulette.common.JSONMessage;

/**
 * Created by sackhorn on 16.05.17.
 */
public class GameStateController {
    ClientStates currentState;
    Overseer mainOverseer;
    public GameStateController()
    {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = Overseer.getInstance();
    }


    public void handleIncomingMessages() throws InterruptedException {
        JSONMessage msg = mainOverseer.communicationThread.incomingMessages.take();

    }


    //USE ONLY WITHIN handleIncomingMessage to ensure that mutex is locked before changing flag
    public void sendMessage(JSONMessage msg)
    {
        assert mainOverseer.comFlagSemaphore.availablePermits() != 0;
        mainOverseer.sendFlag = true;
        mainOverseer.listenFlag = false;
        mainOverseer.communicationThread.outcommingMessages.add(msg);
    }


}

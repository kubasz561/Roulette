package com.kubasz561.roulette.desktopclient.controller;

import java.io.*;
import java.net.Socket;

import com.kubasz561.roulette.common.JSONMessage;


/**
 *
 */
public class ClientCommunicationThread extends Thread {
    private final Socket clientSocket;
    private final ObjectOutputStream clientToServer;
    private final ObjectInputStream serverToClient;

    /**
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public ClientCommunicationThread(String ip, int port) throws IOException
    {
        clientSocket = new Socket(ip,port);
        clientToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        serverToClient = new ObjectInputStream(clientSocket.getInputStream());
    }


    @Override
    public void run() {
        Overseer mainOverseer = Overseer.getInstance();
        JSONMessage message;

        try
        {
            while (mainOverseer.isRunningFlag)
            {
                if(mainOverseer.listenFlag)
                {
                    while (serverToClient.available() > 0)
                    {
                        mainOverseer.comFlagSemaphore.acquire();
                        mainOverseer.listenFlag = false; //TODO: may not be necessary, leave it be for now
                        message = (JSONMessage) serverToClient.readObject();
                        mainOverseer.gameStateController.handleIncomingMessage(message);
                        mainOverseer.listenFlag = true;//TODO: see previous TODO
                        if(!mainOverseer.listenFlag)
                            throw new IOException("listen flag turned off after handling of a message");
                        mainOverseer.comFlagSemaphore.release();
                    }
                }
            }
            close_all();

        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Error while communicating closing");
            close_all();
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(JSONMessage msg) throws InterruptedException, IOException {
        if (!Overseer.getInstance().listenFlag)
            clientToServer.writeObject(msg);
        else
            throw new IOException("Listen Flag is set to true when trying to send message");
    }

    private void close_all(){
        try {
            serverToClient.close();
            clientToServer.close();
            clientSocket.close();
        }catch (IOException e){
            System.out.println("Close unsuccesfull");
            e.printStackTrace();
        }
    }
}
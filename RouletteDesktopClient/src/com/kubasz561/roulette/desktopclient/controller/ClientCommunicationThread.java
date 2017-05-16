package com.kubasz561.roulette.desktopclient.controller;

import java.io.*;
import java.net.Socket;

import com.kubasz561.roulette.common.JSONMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 *
 */
public class ClientCommunicationThread extends Thread {
    private final Socket clientSocket;
    private final ObjectOutputStream clientToServer;
    private final ObjectInputStream serverToClient;
    public final BlockingQueue<JSONMessage> outcommingMessages = new LinkedBlockingQueue<>();
    public final BlockingQueue<JSONMessage> incomingMessages = new LinkedBlockingQueue<>();

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

        try {
            while (mainOverseer.isRunningFlag) {

                if(mainOverseer.listenFlag && !mainOverseer.sendFlag) {
                    while (serverToClient.available() > 0)
                    {
                        mainOverseer.comFlagSemaphore.acquire();
                        mainOverseer.sendFlag = false;
                        mainOverseer.listenFlag = false;

                        message = (JSONMessage) serverToClient.readObject();
                        incomingMessages.add(message);
                        mainOverseer.gameStateController.handleIncomingMessages();
                        mainOverseer.comFlagSemaphore.release();
                    }
                }
                else if(mainOverseer.sendFlag)
                {
                    while((message = outcommingMessages.poll()) != null)
                    {
                       clientToServer.writeObject(message);
                    }
                    mainOverseer.comFlagSemaphore.acquire();
                    mainOverseer.sendFlag = false;
                    mainOverseer.listenFlag = true;
                    mainOverseer.comFlagSemaphore.release();
                }
            }
            close_all();

        } catch (IOException e){
            System.out.println("Error while communicating closing");
            close_all();
        } catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
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
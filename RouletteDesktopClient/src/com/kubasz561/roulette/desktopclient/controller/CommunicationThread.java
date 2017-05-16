package com.kubasz561.roulette.desktopclient.controller;

import java.io.*;
import java.net.Socket;

import com.kubasz561.roulette.common.JSONMessage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;


/**
 *
 */
public class CommunicationThread extends Thread {
    private final Socket clientSocket;
    private final ObjectOutputStream clientToServer;
    private final ObjectInputStream serverToClient;
    public final BlockingQueue<JSONMessage> outcommingMessages = new LinkedBlockingQueue<JSONMessage>();
    public final BlockingQueue<JSONMessage> incomingMessages = new LinkedBlockingQueue<JSONMessage>();
//    private final Semaphore mutex = new Semaphore(1);

    /**
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public CommunicationThread(String ip, int port) throws IOException //Constructor
    {
        clientSocket = new Socket(ip,port);
        clientToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        serverToClient = new ObjectInputStream(clientSocket.getInputStream());
    }


    @Override
    public void run() {

        boolean isRunning = true;
        JSONMessage message;

        try {
            while (isRunning) {

                while(serverToClient.available()>0)
                {
                    message = (JSONMessage)serverToClient.readObject();
                    incomingMessages.add(message);
                    //TODO: Handle disconnection and blocking
                    //TODO: Handle changing game_state here maybe a mutex will be needed
                }

                while((message = outcommingMessages.poll()) != null)
                {
                    clientToServer.writeObject(message);
                }




            }
            close_all();

        } catch (IOException e){
            System.out.println("Error while communicating closing");
            close_all();
        } catch (ClassNotFoundException e) {
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
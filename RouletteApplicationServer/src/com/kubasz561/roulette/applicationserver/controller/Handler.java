package com.kubasz561.roulette.applicationserver.controller;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 */
public class Handler extends Thread{
    private Socket socket;
    private BufferedReader clientToServer;
    private PrintWriter serverToClient;
    private volatile boolean isRunning = true;
    private int clientId;

    /**
     *
     * @param clientSocket
     */
    public Handler(Socket clientSocket, int clientId) {
        try {
            socket = clientSocket;
            clientToServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            serverToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            this.clientId = clientId;

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     *
     */
    @Override
    public void run(){
        serverToClient.write(0);
        serverToClient.println("Hello, you connected as a client " + clientId);
        serverToClient.flush();
        String line="";
        while (isRunning) {

            try {
                line = clientToServer.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);

               try {
                   clientToServer.close();
                   System.out.println("Close successful");

               } catch (Exception e){
                   System.out.print("Close not successful");
               }
               break;
            }

            if (line.toLowerCase().equalsIgnoreCase("exit"))
            {

                System.out.println("Disconnect client" + clientId);
                break;

            }
        }
        try {
            serverToClient.close();
            clientToServer.close();
            socket.close();
            System.out.println("Disconnecting client " + clientId + " successful");

        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}

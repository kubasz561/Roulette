package com.kubasz561.roulette.desktopclient.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 */
public class Client {
    private final BufferedReader userInput;
    private final Socket clientSocket;
    private final PrintWriter clientToServer;
    private final BufferedReader serverToClient;

    /**
     *
     * @param ip
     * @param port
     * @throws IOException
     */
    public Client(String ip,int port) throws IOException //Constructor
    {
        userInput = new BufferedReader(new InputStreamReader(System.in));
        clientSocket = new Socket(ip,port);
        clientToServer = new PrintWriter(clientSocket.getOutputStream(), true);
        serverToClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     *
     * @throws IOException
     */
    public void start() throws IOException {
        String line, ServerAnswer;
        boolean exitFlag = true;

        try {
            System.out.println(serverToClient.readLine());
        } catch (IOException e){
            serverToClient.close();
            System.out.println("Close Successful");
        }

        while (exitFlag) {
            line = userInput.readLine();
            clientToServer.println(line);
            clientToServer.flush();

            if (line.toLowerCase().equals("exit")) {
                exitFlag = false;
            } else {
                try {
                    ServerAnswer = serverToClient.readLine();
                    System.out.println(ServerAnswer);

                } catch (IOException e){
                    serverToClient.close();
                    System.out.println("Close Successful");
                    break;
                }

            }

        }
        userInput.close();
        serverToClient.close();
        clientToServer.close();
        clientSocket.close();

        System.out.println("Disconnect successful");
    }
}
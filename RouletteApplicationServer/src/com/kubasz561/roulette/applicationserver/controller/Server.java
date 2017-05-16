package com.kubasz561.roulette.applicationserver.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


/**
 */
public class Server {
    private static ServerSocket listener;

    public static void main(String[] args) throws IOException {
        int port = 1234;
        System.out.println("The server is running.");

        try {
            listener = new ServerSocket(port);
            int clientId = 1;
            ArrayList<Handler> connectedClientsHandlers = new ArrayList<Handler>();

            while (true) {
                Handler newHandler = new Handler(listener.accept(), clientId);
                connectedClientsHandlers.add(newHandler);
                newHandler.start();
                ++clientId;
            }

        } catch (Exception e) {
            System.out.println("blad klienta");

        } finally {
            listener.close();
            System.out.println("Server stopped");
        }

    }

}

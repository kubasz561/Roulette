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

            while (true) {
                new Handler(listener.accept(), clientId).start();
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

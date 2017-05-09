package com.kubasz561.roulette.desktopclient.controller;

import java.io.*;
/**
 *
 */
public class ClientMain {

    public static void main(String[] args) throws IOException {

        String ip = "127.0.0.1";
        int port = 1234;
        System.out.println("Trying to connect to: " + ip + " " + port);
        try {
            new Client(ip,port).start();

        } catch (IOException e) {
            System.out.println("Unable to connect");

        }

    }
}

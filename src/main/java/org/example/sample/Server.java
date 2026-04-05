package org.example.sample;

import org.example.sample.server.MonoThreadClientHandler;

import java.net.*;
import java.io.*;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(8080);
            System.out.println("Server is running");
            int count = 0;
            clientSocket = server.accept();
            System.out.println("Connect success");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            while (!server.isClosed()) {

                String str;
                if ((str = in.readLine()) != null) {
                    String[] commands = str.split(" ");
                    if (commands[0].equals("close")) {
                        in.close();
                        out.close();
                        System.out.println("Server close");
                        server.close();
                        break;
                    }
                    String event = commands[0];
                    int mY = Integer.parseInt(commands[1]);
                    int mX = Integer.parseInt(commands[2]);
                    //Changes direction to UP when W is pressed
                    MonoThreadClientHandler handler = new MonoThreadClientHandler(clientSocket);
                    handler.handleMove(out, mY, mX, event);
                    System.out.println("data was send");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

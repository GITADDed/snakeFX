package sample.server;

import java.io.*;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {
    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    @Override
    public void run() {
        try {
            int mY;
            int mX;
            String event;
            String[] commands;
            String str;

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
            System.out.println("Out thread created");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            System.out.println("In thread created");

            // Processed where to snake must turn
            System.out.println("Server reading from 'in' channel");
            while (!clientDialog.isClosed()) {
                if ((str = in.readLine()) != null) {
                    commands = str.split(" ");
                    if (commands[0].equals("close")) {
                        out.close();
                        in.close();
                        clientDialog.close();
                        System.out.println("User killed connection");
                        break;
                    }
                    event = commands[0];
                    mY = Integer.parseInt(commands[1]);
                    mX = Integer.parseInt(commands[2]);
                    //Changes direction to UP when W is pressed
                    if (mY == 0 && event.equals("W")) {
                        mX = 0;
                        mY = -1;
                        System.out.println("PUSH UP");
                    }
                    //Changes direction to DOWN when S is pressed
                    else if (mY == 0 && event.equals("S")) {
                        mX = 0;
                        mY = 1;
                        System.out.println("PUSH DOWN");
                    }
                    //Changes direction to Left when A is pressed
                    else if (mX == 0 && event.equals("A")) {
                        mX = -1;
                        mY = 0;
                        System.out.println("PUSH LEFT");
                    }
                    //Changes direction to Right when D is pressed
                    else if (mX == 0 && event.equals("D")) {
                        mX = 1;
                        mY = 0;
                        System.out.println("PUSH RGIHT");
                    }
                    out.write(mY + " " + mX + "\n");
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

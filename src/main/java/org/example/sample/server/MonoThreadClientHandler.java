package org.example.sample.server;

import java.io.*;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {
    private final Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {

        this.clientDialog = client;
    }

    @Override
    public void run() {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
            System.out.println("Out stream get");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
            System.out.println("In stream get");

            // Processed where to snake must turn
            System.out.println("Server reading from 'in' channel");

            listenCommand(in, out);

            in.close();
            out.close();
            clientDialog.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenCommand(BufferedReader in, BufferedWriter out) throws IOException {
        int moveByY;
        int moveByX;
        String event;
        String[] commands;
        String str;

        while (!clientDialog.isClosed()) {
            if ((str = in.readLine()) != null) {
                commands = str.split(" ");

                if (isClientClosedConnection(commands[0])) {
                    break;
                }
                event = commands[0];
                moveByY = Integer.parseInt(commands[1]);
                moveByX = Integer.parseInt(commands[2]);

                handleMove(out, moveByY, moveByX, event);
            }
        }
    }

    public void handleMove(BufferedWriter out, int moveByY, int moveByX, String event) throws IOException {
        //Changes direction to UP when W is pressed
        if (moveByY == 0 && event.equals("W")) {
            moveByX = 0;
            moveByY = -1;
            System.out.println("PUSH UP");
        }
        //Changes direction to DOWN when S is pressed
        else if (moveByY == 0 && event.equals("S")) {
            moveByX = 0;
            moveByY = 1;
            System.out.println("PUSH DOWN");
        }
        //Changes direction to Left when A is pressed
        else if (moveByX == 0 && event.equals("A")) {
            moveByX = -1;
            moveByY = 0;
            System.out.println("PUSH LEFT");
        }
        //Changes direction to Right when D is pressed
        else if (moveByX == 0 && event.equals("D")) {
            moveByX = 1;
            moveByY = 0;
            System.out.println("PUSH RIGHT");
        }
        out.write(moveByY + " " + moveByX + "\n");
        out.flush();
    }

    private boolean isClientClosedConnection(String command) {
        boolean result = command.equals("close");

        if (result) {
            System.out.println("User killed connection");
        }

        return result;
    }
}

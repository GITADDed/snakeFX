package sample;

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
//                clientSocket = server.accept();
//                System.out.println("Connect success");
//                System.out.println("Connect success number " + count++);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
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
                    System.out.println("data was send");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.sample.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    private ExecutorService executeIt;
    private ServerSocket serverSocket;

    public MultiThreadServer(int port, int threads) {
        this.executeIt = Executors.newFixedThreadPool(threads);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isServerShutDown(String command) {
        return command.equals("close");
    }

    public void start() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Server socket was created, and servers' console was on");
            while (!serverSocket.isClosed()) {
                // If admin input command 'close' server will be killed
                if (br.ready()) {
                    System.out.println("Servers commands was written");

                    if (isServerShutDown(br.readLine())) {
                        System.out.println("Server was killed by console command");
                        serverSocket.close();
                        break;
                    }
                }
                // Hold new connection
                Socket clientSocket = serverSocket.accept();
                // Creat new Thread for client
                executeIt.execute(new MonoThreadClientHandler(clientSocket));
                System.out.println("Connection accepted");
            }
            // Shutdown all threads
            executeIt.shutdown();
            System.out.println("threads was shutdown");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

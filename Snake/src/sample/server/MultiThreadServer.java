package sample.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);;

    public static void main(String[] args) {
        String serverCommand;
        try (ServerSocket serverSocket = new ServerSocket(8080);
             // Input for servers' commands from console;
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Server socket was created, and servers' console was on");
            while (!serverSocket.isClosed()) {
                // If admin input command 'close' server will be killed
                if (br.ready()) {
                    System.out.println("Servers commands was written");
                    serverCommand = br.readLine();
                    if (serverCommand.equals("close")) {
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

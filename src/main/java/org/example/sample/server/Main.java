package org.example.sample.server;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        int threads = 2;

        MultiThreadServer server = new MultiThreadServer(port, threads);

        server.start();
    }
}

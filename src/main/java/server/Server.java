package server;

import server.config.Config;
import server.handler.SocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static shared.util.Config config = shared.util.Config.getConfig("mainConfig");

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(config.getProperty("PORT")));
        ExecutorService pool =
                Executors.newFixedThreadPool(
                        Integer.parseInt(config.getProperty("SERVER_ACCEPTING_THREADS"))
                );
        Config.initiate();
        logger.info("server started");
        System.out.println("Ready for new connections");
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.next();
            if (s.equals("close")) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("A client got connected");
            pool.execute(() -> {
                try {
                    new SocketHandler(socket);
                } catch (IOException e) {
                    logger.error("Connection with socket failed");
                    e.printStackTrace();
                }
            });
        }
    }
}

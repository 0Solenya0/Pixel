package server;

import server.config.Config;
import server.handler.BotHandler;
import server.handler.SocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
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
            while (true) {
                String s = scanner.nextLine();
                if (s.equals("close")) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (s.startsWith("load")) {
                    String rest = s.substring(5);
                    Scanner sc = new Scanner(rest);
                    String path = sc.next(), claz = sc.next();
                    pool.execute(() -> {
                        try {
                            new BotHandler(path, claz);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("failed to connect bot");
                            System.out.println("Bot Failed");
                        }
                    });
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

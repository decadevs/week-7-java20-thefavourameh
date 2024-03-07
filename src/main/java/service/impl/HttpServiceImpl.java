package service.impl;

import commons.MyHttpServer;
import service.HttpService;
import utils.HttpHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static commons.MyHttpServer.PORT;

public class HttpServiceImpl implements HttpService {
    @Override
    public void start(int port) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection established");

                HttpHandler httpHandler = new HttpHandler(socket);
                Thread thread = new Thread(httpHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
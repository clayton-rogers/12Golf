package ca.claytonrogers.Server;

import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by clayton on 2015-07-04.
 */
public class Server {
    public static void main (String[] args) {
        System.out.println("Server starting...");

        PlayerDistributor playerDistributor = new PlayerDistributor();

        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT_NUMBER)) {
            try {
                while (true) {
                    System.out.println("Waiting for players to connect...");
                    Socket socket = serverSocket.accept();
                    Connection player = new Connection(socket);
                    playerDistributor.addPlayer(player);
                }
            } catch (IOException e) {
                System.out.println("Something when wrong while the server was listening: " + e);
            }
        } catch (IOException e) {
            System.out.println("Problem starting the server: " + e);
        }
    }
}

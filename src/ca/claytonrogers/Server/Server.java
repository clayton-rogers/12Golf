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

        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT_NUMBER)) {
            try {
                while (true) {
                    System.out.println("Waiting for player 1 to connect...");
                    Socket socket = serverSocket.accept();
                    Connection player1 = new Connection(socket);

                    System.out.println("Waiting for player 2 to connect...");
                    socket = serverSocket.accept();
                    Connection player2 = new Connection(socket);

                    // TODO start the game
                }
            } catch (IOException e) {
                System.out.println("Something when wrong while the server was listening: " + e);
            }
        } catch (IOException e) {
            System.out.println("Problem starting the server.");
        }
    }
}

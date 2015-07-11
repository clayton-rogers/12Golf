package ca.claytonrogers.Server;

import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.Messages.VersionInformation;
import ca.claytonrogers.Common.Messages.VersionInformationAuthenticated;
import ca.claytonrogers.Common.Messages.VersionInformationMismatch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by clayton on 2015-07-04.
 */
public class Server {
    public static void main (String[] args) {
        System.out.println("Server starting...");

        final int NUM_PLAYER = 2;

        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT_NUMBER)) {
            try {
                while (true) {

                    Connection[] players = new Connection[NUM_PLAYER];
                    for (int i = 0; i < NUM_PLAYER; i++) {
                        System.out.println("Waiting for player " + i + " to connect...");
                        Socket socket = serverSocket.accept();
                        players[i] = new Connection(socket);
                    }

                    if (!authenticate(players)) {
                        for (Connection player : players) {
                            player.close();
                        }
                        continue;
                    }
                    GameRunner gameRunner = new GameRunner(players);
                    gameRunner.start();
                }
            } catch (IOException e) {
                System.out.println("Something when wrong while the server was listening: " + e);
            }
        } catch (IOException e) {
            System.out.println("Problem starting the server.");
        }
    }

    /**
     * Authenticates the given list of players, returning true if all players authenticated
     * successfully.
     *
     * @param players The list of players to be authenticated.
     * @return True when all players are authenticated, false if any one of them fails.
     */
    private static boolean authenticate (Connection[] players) {
        for (int i = 0; i < players.length; i++) {
            Connection player = players[i];

            Message message = player.waitForNextMessage();
            if (message.getMessageType() != Message.MessageType.VersionInformation) {
                System.out.println("Received something other than version info for first message: " + message.getMessageType());
                return false;
            }

            int clientVersion = ((VersionInformation)message).getVersionNumber();
            if (clientVersion != Constants.VERSION) {
                System.out.println("Client has different version number");
                player.send(new VersionInformationMismatch(clientVersion, Constants.VERSION));
                return false;
            } else {
                player.send(new VersionInformationAuthenticated(i));
            }
        }

        return true;
    }
}

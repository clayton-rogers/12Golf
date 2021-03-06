package ca.claytonrogers.Golf12.Server;

import ca.claytonrogers.Golf12.Common.Connection;
import ca.claytonrogers.Golf12.Common.Constants;
import ca.claytonrogers.Golf12.Common.Messages.Message;
import ca.claytonrogers.Golf12.Common.Messages.VersionInformation;
import ca.claytonrogers.Golf12.Common.Messages.VersionInformationAuthenticated;
import ca.claytonrogers.Golf12.Common.Messages.VersionInformationMismatch;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class takes input into its player list and then distributes them into a game when there is
 * sufficient players. It runs in its own thread.
 * <p>
 * Created by clayton on 2015-08-14.
 */
class PlayerDistributor implements Runnable {

    private final int NUM_PLAYER = 2;

    private final Queue<Connection> playerList = new ConcurrentLinkedQueue<>();

    public PlayerDistributor() {
        new Thread(this).start();
    }

    public void addPlayer (Connection player) {
        if (authenticatePlayer(player)) {
            playerList.add(player);
        }
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {

            updatedPlayerList();

            if (playerList.size() >= NUM_PLAYER) {
                Connection[] players = new Connection[NUM_PLAYER];

                for (int i = 0; i < NUM_PLAYER; i++) {
                    players[i] = playerList.poll(); // Pop players off till we have enough
                }
                new GameRunner(players);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("PlayerDistributor was interrupted: " + e);
                stop = true;
            }
        }
    }

    private void updatedPlayerList() {

        Queue<Connection> backupList = new ConcurrentLinkedQueue<>();

        Connection player = playerList.poll();
        while (player != null) {
            if (player.isGood()) {
                backupList.add(player);
            }
            player = playerList.poll();
        }

        player = backupList.poll();
        while (player != null) {
            playerList.add(player);
            player = backupList.poll();
        }
    }

    private boolean authenticatePlayer (Connection player) {
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
            player.send(new VersionInformationAuthenticated());
        }

        return true;
    }
}

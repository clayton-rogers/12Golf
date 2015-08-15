package ca.claytonrogers.Server;

import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Constants;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.Messages.VersionInformation;
import ca.claytonrogers.Common.Messages.VersionInformationAuthenticated;
import ca.claytonrogers.Common.Messages.VersionInformationMismatch;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by clayton on 2015-08-14.
 */
public class PlayerDistributor implements Runnable {

    private final int NUM_PLAYER = 2;

    private List<Connection> playerList = new LinkedList<>();

    public PlayerDistributor() {
        new Thread(this).start();
    }

    public synchronized void addPlayer (Connection player) {
        if (authenticatePlayer(player)) {
            playerList.add(player);
        }
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {

            synchronized (this) {
                updatedPlayerList();

                if (playerList.size() >= NUM_PLAYER) {

                    Connection[] players = new Connection[NUM_PLAYER];

                    for (int i = 0; i < NUM_PLAYER; i++) {
                        players[i] = playerList.remove(0); // Pop players off till we have enough
                    }
                    new GameRunner(players);
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("PlayerDistributor was interrupted: " + e);
                stop = true;
            }
        }
    }

    private synchronized void updatedPlayerList() {

        int index = 0;

        while (index < playerList.size()) {
            Connection player = playerList.get(index);
            if (player == null || !player.isGood()) {
                playerList.remove(index);
            } else {
                index++;
            }
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

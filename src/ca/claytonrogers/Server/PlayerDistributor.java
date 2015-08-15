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
                if (playerList.size() >= 2) {

                    Connection[] players = new Connection[NUM_PLAYER];
                    boolean isGood = true;
                    for (int i = 0; i < NUM_PLAYER; i++) {
                        Connection player;
                        do {
                            if (playerList.isEmpty()) {
                                for (Connection p : players) {
                                    if (p != null) {
                                        playerList.add(p);
                                    }
                                }
                                isGood = false;
                                break;
                            } else {
                                player = playerList.remove(0);
                                players[i] = player;
                            }
                        } while (player != null && !player.isGood());
                    }
                    if (isGood) {
                        new GameRunner(players);
                    }
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

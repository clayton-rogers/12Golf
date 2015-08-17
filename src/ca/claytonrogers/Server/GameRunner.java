package ca.claytonrogers.Server;

import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.Messages.PlayerInfo;

import java.io.IOException;

/**
 * GameRunner creates the initial game state and send it to all the players. It then parrots
 * information from any given client to all the other clients.
 *
 * Created by clayton on 2015-07-10.
 */
class GameRunner implements Runnable {

    private final Connection[] players;
    private volatile boolean continueGame = true;

    public GameRunner(Connection[] players) {
        this.players = players;

        for (int i = 0; i < players.length; i++) {
            players[i].send(new PlayerInfo(i, players.length));
        }

        new Thread(this).start();
    }

    @Override
    public void run() {

        // Keep getting message and parroting them to all players.
        while (continueGame) {
            for (int i = 0; i < players.length; i++) {
                Message message = players[i].getMessage();
                if (!players[i].isGood()) {
                    continueGame = false;
                }

                if (message != null) {
                    sendToAllExcept(i, message);
                }
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                System.out.println("GameRunner was interrupted while waiting.");
            }
        }

        // Once one of the players has a connection problem, disconnect all the players.
        for (Connection player : players) {
            try {
                player.close();
            } catch (IOException e) {
                System.out.println("There was an issue disconnecting one of the players after the game. (This message is expected at least once.)");
            }
        }
    }

    private void sendToAllExcept (int player, Message message) {
        for (int i = 0; i < players.length; i++) {
            if (i != player) {
                players[i].send(message);
                if (!players[i].isGood()) {
                    continueGame = false;
                }
            }
        }
    }
}

package ca.claytonrogers.Server;

import ca.claytonrogers.Common.Connection;
import ca.claytonrogers.Common.Messages.Message;
import ca.claytonrogers.Common.Messages.StateUpdate;
import ca.claytonrogers.Common.State;

/**
 * GameRunner creates the initial game state and send it to all the players. It then parrots
 * information from any given client to all the other clients.
 *
 * Created by clayton on 2015-07-10.
 */
public class GameRunner implements Runnable {

    private Thread thread;
    private Connection[] players;
    private volatile boolean continueGame = true;

    public GameRunner(Connection[] players) {
        this.players = players;

        thread = new Thread(this);
        thread.start();
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {

        // Create the initial state and send it to all players, they'll take it from there.
        State state = new State(players.length);
        sendToAllExcept(-1, new StateUpdate(state));

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

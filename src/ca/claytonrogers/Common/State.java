package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-05.
 */
public class State {

    private Deck   discardPile = new Deck();
    private Deck   drawPile    = new Deck();
    private Hand[] playerHands = new Hand[4];

    public State() {
        for (int i = 0; i < 4; i++) {
            playerHands[i] = new Hand();
        }

        // TODO Create a full deck and deal it out.
    }

    public Deck getDiscardPile() {
        return discardPile;
    }

    public Deck getDrawPile() {
        return drawPile;
    }

    public Hand[] getPlayerHands() {
        return playerHands;
    }
}

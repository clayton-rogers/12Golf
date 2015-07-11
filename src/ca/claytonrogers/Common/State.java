package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-05.
 */
public class State {

    private int    numberOfPlayers = 0;
    private Deck   discardPile = new Deck();
    private Deck   drawPile    = new Deck();
    private Hand[] playerHands;

    public State(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;

        playerHands = new Hand[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playerHands[i] = new Hand();
        }

        drawPile = Deck.getShuffledDeck();
        // Deal the players
        for (int cardIndex = 0; cardIndex < Constants.NUMBER_OF_CARDS; cardIndex++) {
            for (int player = 0; player < numberOfPlayers; player++) {
                playerHands[player].setCard(cardIndex, drawPile.pop());
            }
        }
        // Add a card to the discard
        discardPile.push(drawPile.pop());
        // What remains of the deck is in the draw pile
    }

    public State (State copy) {
        numberOfPlayers = copy.getNumberOfPlayers();
        discardPile = new Deck(copy.getDiscardPile());
        drawPile    = new Deck(copy.getDrawPile());
        playerHands = new Hand[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playerHands[i] = new Hand(copy.getPlayerHands()[i]);
        }
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

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}

package ca.claytonrogers.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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

    public static State read(DataInputStream in) throws IOException {
        // Get the number of players and create the state
        int numberOfPlayers = in.readByte();
        State state = new State(numberOfPlayers);

        // Get the discard deck
        Deck discardDeck = Deck.read(in);

        // Get the draw deck
        Deck drawDeck = Deck.read(in);

        // Get the hands
        Hand[] hands = new Hand[numberOfPlayers];
        for (int player = 0; player < numberOfPlayers; player++) {
            Hand hand = new Hand();
            for (int i = 0; i < Constants.NUMBER_OF_CARDS; i++) {
                hand.setCard(i, new Card(in.readByte(), false));
            }
            hands[player] = hand;
        }

        // Set the state to the received data
        state.numberOfPlayers = numberOfPlayers;
        state.discardPile = discardDeck;
        state.drawPile = drawDeck;
        state.playerHands = hands;

        return state;
    }

    public static void write(DataOutputStream out, State state) throws IOException {
        // Number of players is most important
        out.writeByte(state.numberOfPlayers);

        // Write the discard deck
        Deck.write(out, state.getDiscardPile());

        // Write the draw deck
        Deck.write(out, state.getDrawPile());

        // Write the hands
        for (int player = 0; player < state.getNumberOfPlayers(); player++) {
            Hand hand = state.getPlayerHands()[player];
            for (int i = 0; i < Constants.NUMBER_OF_CARDS; i++) {
                out.writeByte(hand.getCard(i).value);
            }
        }

        // Flushing is done at the message level
    }
}

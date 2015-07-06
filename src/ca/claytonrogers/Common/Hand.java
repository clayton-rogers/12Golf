package ca.claytonrogers.Common;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by clayton on 2015-07-05.
 */
public class Hand {

    private Card[] hand = new Card[Constants.NUMBER_OF_CARDS];

    public Hand() {}

    public Hand(Hand copy) {
        for (int i = 0; i < Constants.NUMBER_OF_CARDS; i++) {
            hand[i] = copy.hand[i];
        }
    }

    public void setCard(int index, Card card) {
        if (index < 0 || index > Constants.NUMBER_OF_CARDS-1) {
            throw new IllegalStateException("Tried to set an out of range card of a hand.");
        }
        hand[index] = new Card(card);
    }

    public Card getCard(int index) {
        if (index < 0 || index > Constants.NUMBER_OF_CARDS-1) {
            throw new IllegalStateException("Tried to get an out of range card of a hand.");
        }

        return hand[index];
    }


}

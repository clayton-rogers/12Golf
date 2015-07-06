package ca.claytonrogers.Common;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by clayton on 2015-07-05.
 */
public class Hand {

    private List<Card> hand = new ArrayList<>(12);

    public void setCard(int index, Card card) {
        if (index < 0 || index > 11) {
            throw new IllegalStateException("Tried to set an out of range card of a hand.");
        }
        hand.set(index,card);
    }

    public Card getCard(int index) {
        if (index < 0 || index > 11) {
            throw new IllegalStateException("Tried to get an out of range card of a hand.");
        }

        return hand.get(index);
    }


}

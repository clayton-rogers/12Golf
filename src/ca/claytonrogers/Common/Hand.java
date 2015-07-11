package ca.claytonrogers.Common;


import java.util.Arrays;

/**
 * This class represents a hand dealt to a player.
 *
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

    /**
     * Sets the given location with the given card. Note: only empty locations can have cards
     * placed in them.
     *
     * @param index Location to set.
     * @param card The given card.
     */
    public void setCard(int index, Card card) {
        validateIndex(index);
        if (hand[index] != null) {
            throw new IllegalArgumentException("Tried to set a location that already has a card.");
        }
        hand[index] = card;
    }

    /**
     * Clears the hand, returning the array of cards it contained.
     *
     * @return The cards that used to be in the hand.
     */
    public Card[] clearHand() {
        Card[] temp = hand;
        Arrays.fill(hand,null);
        return temp;
    }

    /**
     * Returns a reference to the required card.
     *
     * @param index The index of the required card.
     * @return The card that was required.
     */
    public Card getCard(int index) {
        validateIndex(index);
        return hand[index];
    }

    /**
     * Flips a card face side up. If the card is already face side up, it has no effect.
     *
     * @param index The index of the card to flip.
     */
    public void flipCard (int index) {
        validateIndex(index);

        hand[index].isFaceUp = true;
    }

    /**
     * Replaces a card in the hand with the given card. Returns the card that used to be
     * in the hand.
     *
     * @param index The location in the hand to replace.
     * @param cardRoReplaceWith The card to replace with.
     * @return The card that was removed from the hand.
     */
    public Card replaceCard (int index, Card cardRoReplaceWith) {
        validateIndex(index);

        Card temp = hand[index];
        hand[index] = cardRoReplaceWith;
        return temp;
    }

    /**
     * Calculates the total score of all face up cards in a hand.
     *
     * @return The score of the hand.
     */
    public int getHandScore() {
        int handWidth = Constants.NUMBER_OF_CARDS/2;
        int handScore = 0;
        for (int i = 0; i < handWidth; i++) {
            if (hand[i].isFaceUp && hand[i+handWidth].isFaceUp) {
                handScore += getPairScore(hand[i], hand[i+handWidth]);
            } else {
                handScore += hand[i].score() + hand[i+handWidth].score();
            }
        }

        return handScore;
    }

    /**
     * Finds the score of a column of two cards.
     *
     * @param a The first card.
     * @param b The second card.
     * @return The score of the two cards together.
     */
    private int getPairScore(Card a, Card b) {
        // Pair of anything is zero
        // Pair of kings is -2
        // Anything else is face value

        if (a.isFaceUp && b.isFaceUp) {
            if (a.value != b.value) {
                return a.score() + b.score();
            } else {
                if (a.value == 13) {
                    return -2;
                } else {
                    return 0;
                }
            }
        } else {
            throw new IllegalArgumentException("Both cards must be face up to get pair score.");
        }
    }

    /**
     * Validates that an index actually falls in the number of cards range.
     *
     * @param index The index to be validated.
     */
    private void validateIndex (int index) {
        if (index < 0 || index > Constants.NUMBER_OF_CARDS-1) {
            throw new IllegalStateException("Tried to use an out of range index in a hand.");
        }
    }
}

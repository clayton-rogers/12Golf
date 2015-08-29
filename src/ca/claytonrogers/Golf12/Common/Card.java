package ca.claytonrogers.Golf12.Common;

/**
 * Represent a single card on the table. Can be face up or down. Can have a value between 1 and 13
 * (Ace and King).
 * <p>
 * Created by clayton on 2015-07-05.
 */
public class Card {
    public int value = 0;
    public boolean isFaceUp = false;
    public Suit suit;

    public enum Suit {
        spades,
        clubs,
        hearts,
        diamonds;

        @Override
        public String toString() {
            switch (this) {
                case spades:
                    return "\u2660";
                case clubs:
                    return "\u2663";
                case hearts:
                    return "\u2665";
                case diamonds:
                    return "\u2666";
            }

            throw new IllegalStateException("");
        }
    }

    public Card(int value, boolean isFaceUp, Suit suit) {
        this.value = value;
        this.isFaceUp = isFaceUp;
        this.suit = suit;
    }

    public int score() {
        if (isFaceUp) {
            if (value == 13) {
                return 0;
            } else {
                return value;
            }
        } else {
            return 0;
        }
    }

    public String faceValue() {
        if (value >= 2 && value <= 10) {
            return String.valueOf(value);
        } else if (value == 1) {
            return "A";
        } else if (value == 11) {
            return "J";
        } else if (value == 12) {
            return "Q";
        } else if (value == 13) {
            return "K";
        } else if (value == 14) {
            return "Jo"; // Joker
        } else if (value == -1) {
            return ""; // This is used for a blank card face
        } else {
            System.out.println("Could not find the face value of a card: " + value);
            return "";
        }
    }
}

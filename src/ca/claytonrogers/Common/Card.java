package ca.claytonrogers.Common;

/**
 * Created by clayton on 2015-07-05.
 */
public class Card {
    public int value = 0;
    public boolean isFaceUp = false;

    public Card(int value, boolean isFaceUp) {
        this.value = value;
        this.isFaceUp = isFaceUp;
    }

    public Card(Card copy) {
        value    = copy.value;
        isFaceUp = copy.isFaceUp;
    }

    public int score() {
        if (isFaceUp) {
            return value;
        } else {
            return 0;
        }
    }

    public String faceValue() {
        if (value >= 1 && value <= 10) {
            return String.valueOf(value);
        } else if (value == 11) {
            return "J";
        } else if (value == 12) {
            return "Q";
        } else if (value == 13) {
            return "K";
        } else if (value == 14) {
            return "X"; // Joker
        } else {
            System.out.println("Could not find the face value of a card: " + value);
            return "";
        }
    }
}

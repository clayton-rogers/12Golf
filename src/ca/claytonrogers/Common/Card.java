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
}
